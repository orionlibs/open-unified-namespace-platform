# UserLoginApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**login**](UserLoginApi.md#login) | **POST** /v1/users/login | Login user |


<a id="login"></a>
# **login**
> Object login(loginRequest)

Login user

Login user

### Example
```java
// Import classes:
import io.github.orionlibs.sdk.user.invoker.ApiClient;
import io.github.orionlibs.sdk.user.invoker.ApiException;
import io.github.orionlibs.sdk.user.invoker.Configuration;
import io.github.orionlibs.sdk.user.invoker.models.*;
import io.github.orionlibs.sdk.user.api.UserLoginApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    UserLoginApi apiInstance = new UserLoginApi(defaultClient);
    LoginRequest loginRequest = new LoginRequest(); // LoginRequest | 
    try {
      Object result = apiInstance.login(loginRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserLoginApi#login");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **loginRequest** | [**LoginRequest**](LoginRequest.md)|  | |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | User logged in |  -  |
| **400** | Invalid input |  -  |

