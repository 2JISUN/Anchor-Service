package com.anchor.domain.user.api.controller;

import com.anchor.domain.mentoring.api.controller.request.MentoringReviewInfo;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.anchor.domain.user.api.service.UserService;
import com.anchor.domain.user.api.service.response.AppliedMentoringInfo;
import com.anchor.domain.user.api.service.response.UserInfoResponse;
import com.anchor.global.auth.SessionUser;
import com.anchor.global.util.view.ViewResolver;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserViewController {

  private final UserService userService;
  private final ViewResolver viewResolver;

  @GetMapping("/me")
  public String getInfo(Model model, HttpSession httpSession) {
    SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user"); //email, nickname, image
    String email = sessionUser.getEmail();
    UserInfoResponse userInfoResponse = userService.getProfile(email);
    model.addAttribute("user", userInfoResponse);
    return "내 프로필 페이지 조회";
  }

  /**
   * 멘토링 신청내역을 조회합니다.
   */
  @GetMapping("/me/applied-mentorings")
  public String appliedMentoringList(@PageableDefault(sort = {"id"}, direction = Direction.DESC) Pageable pageable,
      HttpSession session, Model model) {

    SessionUser sessionUser = SessionUser.getSessionUser(session);

    Page<AppliedMentoringInfo> appliedMentoringInfoList = userService.loadAppliedMentoringList(
        sessionUser, pageable);

    model.addAttribute("mentoringApplications", appliedMentoringInfoList);
    return viewResolver.getViewPath("user", "user-mentoring-application");
  }
  
    @GetMapping("/me/applied-mentorings/{id}/review")
  public String review(@PathVariable Long id) {
    return "리뷰 작성 페이지로 이동";
  }

  @PostMapping("/me/applied-mentorings/{id}/review")
  public String reviewProcess(@PathVariable Long id, @ModelAttribute MentoringReviewInfo mentoringReviewInfo) {
    userService.writeReview(id, mentoringReviewInfo);
    return "리뷰 작성 성공";
  }

}
