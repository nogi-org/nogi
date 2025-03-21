package kr.co.nogibackend.interfaces.admin;

import kr.co.nogibackend.domain.admin.AdminService;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminGetController {

  private final AdminService adminService;

  @GetMapping("/user-info")
  public ResponseEntity<?> getUserInfo() {
    return Response.success(adminService.getUserInfo());
  }

}
