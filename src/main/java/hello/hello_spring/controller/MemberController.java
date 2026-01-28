package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
    // 의존성 주입(DI) : 스프링 컨테이너에 있는 멤버 서비스를 가져와서 자동으로 연결
    @Autowired
    private MemberService memberService;

    // 회원 가입 폼 화면으로 이동
    @GetMapping("/members/new")
    public String createForm() {return "members/createMemberForm";}

    // 회원 컨트롤러에서 회원을 실제로 등록하는 기능
    @PostMapping("/members/new")
    public String create(Member vo){
        // 회원 가입 비지니스 로직 실행
        memberService.join(vo);
        // 홈 화면으로 이동
        return  "redirect:/";
    }

    // 홈 화면으로 이동
    @GetMapping("/members")
    public String allMemberList(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
