# policy.rego 
package istio.authz 
 
default allow = false 
 
allow { 
	# Allow requests from authenticated users 
	input.attributes.request.http.headers["authorization"] 
	# Add your custom authorization logic here 
}