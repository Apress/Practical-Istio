// src/header_plugin.cc
#include "proxy_wasm_intrinsics.h"

class HeaderPluginRootContext : public RootContext {
public:
  explicit HeaderPluginRootContext(uint32_t id, std::string_view root_id)
      : RootContext(id, root_id) {}

  bool onConfigure(size_t configuration_size) override {
    // Access the configuration data if available
    if (configuration_size > 0) {
      auto configuration = getBufferBytes(WasmBufferType::PluginConfiguration, 0, configuration_size);
      std::string config_string(configuration->view());
      
      // Parse the configuration string (expect "header_name:header_value")
      auto delimiter_pos = config_string.find(':');
      if (delimiter_pos != std::string::npos) {
        header_name_ = config_string.substr(0, delimiter_pos);
        header_value_ = config_string.substr(delimiter_pos + 1);
        LOG_INFO("Configured HeaderPlugin with " + header_name_ + ":" + header_value_);
        return true;
      } else {
        LOG_WARN("Invalid configuration format. Expected 'header_name:header_value'");
      }
    }
    
    // Use defaults if no valid configuration is provided
    header_name_ = "x-wasm-custom";
    header_value_ = "istio-plugin-example";
    LOG_INFO("Using default configuration: " + header_name_ + ":" + header_value_);
    return true;
  }

  std::string header_name_;
  std::string header_value_;
};

class HeaderPluginContext : public Context {
public:
  explicit HeaderPluginContext(uint32_t id, RootContext* root)
      : Context(id, root), root_(static_cast<HeaderPluginRootContext*>(root)) {}

  FilterHeadersStatus onRequestHeaders(uint32_t, bool) override {
    // Add the configured header to outgoing requests
    addRequestHeader(root_->header_name_, root_->header_value_);
    LOG_INFO("Added header " + root_->header_name_ + ":" + root_->header_value_);
    return FilterHeadersStatus::Continue;
  }

private:
  HeaderPluginRootContext* root_;
};

static RegisterContextFactory register_HeaderPlugin(
    CONTEXT_FACTORY(HeaderPluginContext),
    ROOT_FACTORY(HeaderPluginRootContext),
    "header_plugin");