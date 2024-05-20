//package com.UserService;
//
//import com.UserService.dto.UserDto;
//import com.UserService.entity.User;
//import com.UserService.exception.UserAlreadyExistsException;
//import com.UserService.repository.UserRepository;
//import com.UserService.service.UserService;
//import com.UserService.serviceImpl.UserServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTests {
//
//    @Mock
//    private UserRepository userRepo;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    private User user;
//    private UserDto userDto;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        user = new User();
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//
//        userDto = new UserDto();
//        userDto.setUsername("testuser");
//        userDto.setEmail("test@example.com");
//    }
//
//    @Test
//    void testRegisterUser() {
//        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        userService.registerUser(userDto);
//
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void testRegisterUserThrowsException() {
//        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userDto));
//    }
//
//    @Test
//    void testFetchUserByUsername() {
//        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
//
//        UserDto fetchedUser = userService.fetchUser("testuser", UserService.FetchType.USERNAME);
//
//        assertEquals(userDto.getUsername(), fetchedUser.getUsername());
//    }
//
//    @Test
//    void testFetchUserByEmail() {
//        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//        UserDto fetchedUser = userService.fetchUser("test@example.com", UserService.FetchType.EMAIL);
//
//        assertEquals(userDto.getEmail(), fetchedUser.getEmail());
//    }
//
//    @Test
//    void testUpdateUser() {
//        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//        boolean isUpdated = userService.updateUser(userDto);
//
//        assertTrue(isUpdated);
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
////    @Test
////    void testUpdateUserThrowsException() {
////        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
////
////        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userDto));
////    }
//
//    @Test
//    void testDeleteUser() {
//        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
//
//        boolean isDeleted = userService.deleteUser("test@example.com");
//
//        assertTrue(isDeleted);
//        verify(userRepo, times(1)).deleteByEmail(anyString());
//    }
//
////    @Test
////    void testDeleteUserThrowsException() {
////        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
////
////        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser("nonexistent@example.com"));
////    }
//
//    @Test
//    void testGetAllUsers() {
//        when(userRepo.findAll()).thenReturn(List.of(user));
//
//        List<UserDto> users = userService.getAllUsers();
//
//        assertFalse(users.isEmpty());
//        assertEquals(1, users.size());
//        assertEquals(userDto.getUsername(), users.get(0).getUsername());
//    }
//}
