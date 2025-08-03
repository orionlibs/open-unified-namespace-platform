# UsersApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**saveUser**](UsersApi.md#saveUser) | **POST** /v1/users | Save user |


<a id="saveUser"></a>
# **saveUser**
> Object saveUser(userRegistrationRequest)

Save user

Save user

### Example
```java
// Import classes:
import io.github.orionlibs.sdk.user.invoker.ApiClient;
import io.github.orionlibs.sdk.user.invoker.ApiException;
import io.github.orionlibs.sdk.user.invoker.Configuration;
import io.github.orionlibs.sdk.user.invoker.models.*;
import io.github.orionlibs.sdk.user.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    UsersApi apiInstance = new UsersApi(defaultClient);
    UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(); // UserRegistrationRequest | 
    try {
      Object result = apiInstance.saveUser(userRegistrationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#saveUser");
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
| **userRegistrationRequest** | [**UserRegistrationRequest**](UserRegistrationRequest.md)|  | |

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
| **201** | User saved |  -  |
| **400** | Invalid input |  -  |
| **409** | Duplicate username found |  -  |

