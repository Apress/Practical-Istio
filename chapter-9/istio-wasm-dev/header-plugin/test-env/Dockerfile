FROM envoyproxy/envoy:v1.23.1

COPY header_plugin.wasm /etc/envoy/header_plugin.wasm
COPY envoy.yaml /etc/envoy/envoy.yaml

EXPOSE 9901
EXPOSE 10000

CMD ["envoy", "-c", "/etc/envoy/envoy.yaml", "--service-cluster", "wasm-test", "--log-level", "debug"]
