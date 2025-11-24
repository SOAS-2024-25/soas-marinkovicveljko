package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.UserDto;

@FeignClient("users-service")
public interface UsersProxy {
	
	@GetMapping("/users/email")
	UserDto getUserByEmail(@RequestParam("email") String email);
}
