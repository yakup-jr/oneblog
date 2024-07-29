package com.oneblog.internal.users;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

//	private final UserService userService;
//
//
//	public UserController(UserService userService) {
//		this.userService = userService;
//	}
//
//	@PostMapping
//	ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
//		try {
//			User savedUser = userService.saveUser(userDto);
//			return ResponseEntity.status(201).contentType(
//					                     MediaType.valueOf("application/net.whtoy.oneblog.user.v1+json"))
//			                     .body(savedUser);
//		} catch (RuntimeException runtimeException) {
//			return ResponseEntity.status(400).contentType(
//					                     MediaType.valueOf("application/net.whtoy.oneblog" + ".user.v1+json"))
//			                     .body(runtimeException);
//		}
//
//	}
//
//	@GetMapping
//	ResponseEntity<?> findAllUsers(
//			@RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
//			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
//		List<User> usersEntity = userService.findAllUsers(PageRequest.of(pageNum, pageSize));
//
//		return ResponseEntity.status(200).contentType(
//				MediaType.valueOf("application/net.whtoy.oneblog.user.v1+json")).body(usersEntity);
//	}
//
//	@GetMapping("/{id}")
//	ResponseEntity<?> findUserById(@PathVariable String id) {
//		try {
//			User user = userService.findUserById(UUID.fromString(id));
//			return ResponseEntity.ok().contentType(
//					                     MediaType.valueOf("application/net.whtoy" + ".oneblog.user.v1+json"))
//			                     .body(user);
//		} catch (ApiRequestException apiRequestException) {
//			return ResponseEntity.status(404).contentType(
//					                     MediaType.valueOf("application/net.whtoy.oneblog" + ".user.v1+json"))
//			                     .body(apiRequestException);
//		} catch (RuntimeException exception) {
//			return ResponseEntity.status(400).contentType(
//					                     MediaType.valueOf("application/net.whtoy.oneblog.user.v1+json"))
//			                     .body(exception);
//		}
//	}
//
//	@PutMapping("/{id}")
//	ResponseEntity<?> updateUserById(
//			@PathVariable String id, @RequestBody UserDto userDto) {
//		try {
//			userService.updateUserById(UUID.fromString(id), userDto);
//
//			return ResponseEntity.status(204).contentType(
//					MediaType.valueOf("application/net" + ".whtoy.oneblog.user.v1+json")).build();
//		} catch (RuntimeException exception) {
//			return ResponseEntity.status(400).contentType(
//					                     MediaType.valueOf("application/net" + ".whtoy.oneblog.user.v1+json"))
//			                     .body(exception.getMessage());
//		}
//	}
//
//	//todo: add validation for body
//	@PatchMapping("/{id}")
//	ResponseEntity<?> partialUpdateUserById(@PathVariable String id, @RequestBody UserDto userDto) {
//		try {
//			User user = userMapper.mapTo(userDto);
//			userService.partialUpdateUserById(UUID.fromString(id), user);
//			return ResponseEntity.status(204).contentType(
//					MediaType.valueOf("application/net.whtoy" + ".oneblog.user.v1+json")).build();
//		} catch (ApiRequestException apiRequestException) {
//			return ResponseEntity.status(404).contentType(
//					                     MediaType.valueOf("application/net.whtoy" + ".oneblog.user.v1+json"))
//			                     .body(apiRequestException);
//		} catch (RuntimeException runtimeException) {
//			return ResponseEntity.status(400).contentType(
//					                     MediaType.valueOf("application/net.whtoy" + ".oneblog.user.v1+json"))
//			                     .body(runtimeException);
//		}
//	}
//
//	@DeleteMapping("/{id}")
//	ResponseEntity<?> deleteUserById(@PathVariable String id) {
//		try {
//			userService.deleteUserById(UUID.fromString(id));
//			return ResponseEntity.status(204).contentType(
//					MediaType.valueOf("application/net.whtoy.oneblog.user.v1+json")).build();
//		} catch (ApiRequestException apiRequestException) {
//			return ResponseEntity.status(404).contentType(
//					                     MediaType.valueOf("application/net.whtoy.oneblog.user.v1+json"))
//			                     .body(apiRequestException);
//		} catch (RuntimeException exception) {
//			return ResponseEntity.status(400).contentType(
//					                     MediaType.valueOf("application/net.whtoy.oneblog.user.v1+json"))
//			                     .body(exception);
//		}
//	}
}
