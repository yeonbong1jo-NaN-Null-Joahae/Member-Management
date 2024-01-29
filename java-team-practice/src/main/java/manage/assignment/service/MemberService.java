package manage.assignment.service;

import manage.assignment.aggregate.Member;
import manage.assignment.repository.MemberRepository;

import java.util.ArrayList;

/* 설명. 트랜잭션 성공/실패 여부 확인 및 회원 관련 비즈니스 로직 처리하는 클래스 */
public class MemberService {

    private final MemberRepository mr = new MemberRepository();

    public MemberService() {
    }

    public void selectAllMembers() {
        ArrayList<Member> selectedMembers = mr.selectAllMembers();

        /* 설명. 회원이 한명도 없어서 조회 결과가 없더라도 ArrayList객체는 넘어온다. (Empty 상태로) */
        if (!selectedMembers.isEmpty()) {       // 회원이 한명이라도 조회된다면
            System.out.println("===== service까지 잘 반환되어 오나 확인 =====");
            for(Member m: selectedMembers) {
                System.out.println(m);
            }
            return ;                            // 이후 코드와 상관 없이 메소드 종료
        }
        /* 설명. 조건이 맞지 않아(회원이 조회되지 않아) 출력을 하는 구문(위의 조건이 맞으면 실행되지 않음)(feat. else 안 쓰기) */
        System.out.println("슬프게도 우리 사이트는 아직 회원이 없습니다. ㅠㅠ");

    }

    /* 설명. 전달된 회원번호를 활용해 repository에 있는 memberList로부터 해당 회원 찾아 반환 받기 */
    public Member selectMember(int memNo) {
        Member selectedMember = mr.selectMember(memNo);

        if (selectedMember == null) {
            System.out.println("그런 회원이 없네요 ㅠㅠ");
        } else {
            System.out.println("조회된 회원은: " + selectedMember);
        }

        return selectedMember;
    }

    /* 설명. 입력받아 넘어온 회원이 가질 번호를 만들고 추가 후 repository로 전달 후 결과 화인 */
    public void registMember(Member member) {
//        System.out.println("사용자가 입력해서 넘겨준 Member 확인: " + member);

        int lastNumberNo = mr.selectLastMemberNo();
        member.setMemNo(lastNumberNo + 1);

        int result = mr.registMember(member);
        if (result == 1) {
            System.out.println(member.getId() + "님의 회원 가입이 성공하였습니다.");
        }
    }

    public void deleteMember(int memNo) {
        int result = mr.deleteMember(memNo);
        if (result > 0) {
            System.out.println(memNo + "번 회원 탈퇴를 성공하였습니다.");
            return;
        }

        System.out.println("회원 탈퇴에 실패하였습니다.");
    }


    /* 설명. 로그인 결과 출력 */
    public void loginCheck(String id, String pwd){
        int result = mr.loginCheck(mr.findMember(id),pwd);
        if(result == 1){
            System.out.println("존재하지 않는 아이디입니다.");
        } else if (result == 2) {
            System.out.println("로그인에 성공했습니다.");
        } else if(result == 3){
            System.out.println("비밀번호가 틀렸습니다.");
        }
    }


    public void modifyMember(Member member) {
        int result = mr.modifyMember(member);
        if (result > 0) {
            System.out.println("\n" + member.getMemNo() + "번 회원정보 수정을 성공하였습니다.");
            return;
        }
        System.out.println("회원정보 수정에 실패하였습니다.");
    }
    public void modifyMember() {
    }

    public void loginCheck() {
    }

    /* 설명. searchMemberByHobby 메소드 추가 */
    public void searchMemberByHobby(String hobby) {
        /* 메모
            1. Application의 chooseHobby 메소드에서 hobby를 전달 받음
            2. Repository에서 취미가 일치하는 멤버의 객체를 받아와 ArrayList에 저장
            3. 해당 멤버 배열을 출력
         */

        ArrayList<Member> sharedHobbyMembers = mr.searchMemberByHobby(hobby);

        if(!sharedHobbyMembers.isEmpty()){
            System.out.println("============ " + hobby + " 취미를 가진 멤버 목록 ============");
            for (Member m : sharedHobbyMembers){
                System.out.println(m);
            }
        } else
            System.out.println("해당 취미를 가진 회원이 없네요~");
    }

    public void resetPassword() {
    }
}
