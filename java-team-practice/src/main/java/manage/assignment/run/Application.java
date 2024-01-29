package manage.assignment.run;

import manage.assignment.aggregate.BloodType;
import manage.assignment.aggregate.Member;
import manage.assignment.service.MemberService;

import java.util.Scanner;

/* 설명. 프로그램 실행 및 메뉴 출력과 사용자의 입력을 받을 View에 해당하는 클래스 */
public class Application {

    private static final MemberService mm = new MemberService();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("======= 회원 관리 프로그램 =======");
            System.out.println("1. 모든 회원 정보 보기");
            System.out.println("2. 회원 찾기");
            System.out.println("3. 회원 가입");
            System.out.println("4. 회원 탈퇴");
            System.out.println("8. 아이디/비밀번호 변경");
            System.out.println("9. 프로그램 종료");
            System.out.print("메뉴를 선택해 주세요: ");
            int input = sc.nextInt();

            switch (input) {
                case 1: mm.selectAllMembers();
                    break;
                case 2: mm.selectMember(chooseMemberNo());
                    break;
                case 3: mm.registMember(signUp());
                    break;
                case 4: mm.deleteMember(chooseMemberNo());
                    break;
                case 8: mm.find(findIdOrPw());
                    break;
                case 9:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("번호를 다시 제대로 입력해 주세요");
            }
        }
    }

    private static Member findIdOrPw() {
        Scanner sc = new Scanner(System.in);
        System.out.println("찾으려는 내용이 무엇인가요?");
        System.out.print("1. 아이디        2. 비밀번호");
        int input1 = sc.nextInt();

        if (input1 == 1) {
            Member findId = null;
            System.out.print("나이를 입력하세요 : ");
            int age = sc.nextInt();

            System.out.print("가지고 있는 취미 개수를 입력하세요(숫자로 1 이상): ");
            int length = sc.nextInt();
            sc.nextLine();
            String[] hobbies = new String[length];
            for (int i = 0; i < hobbies.length; i++) {
                System.out.print((i + 1) + "번째 취미를 입력하세요: ");
                String input3 = sc.nextLine();
                hobbies[i] = input3;
            }

            System.out.print("혈액형을 입력하세요 : ");
            String bloodtype = sc.nextLine();

            findId = new Member(age, hobbies, bloodtype);
            return findId;

        } else {
            Member findPw = null;
            System.out.println("아이디를 입력하세요 : ");
            String id = sc.nextLine();
            sc.nextLine();

            System.out.println("가지고 있는 취미 개수를 입력하세요(숫자로 1 이상) : ");
            int length = sc.nextInt();
            sc.nextLine();
            String[] hobbies = new String[length];
            for (int i = 0; i < hobbies.length; i++) {
                System.out.print((i + 1) + "번째 취미를 입력하세요: ");
                String input4 = sc.nextLine();
                hobbies[i] = input4;
            }
            findPw = new Member(id, hobbies);
            return findPw;
        }
    }

    /* 설명. 회원 1명 조회를 위해 해당 회원 번호를 입력받아 반환하는 메소드 */
        private static int chooseMemberNo() {
            Scanner sc = new Scanner(System.in);
            System.out.print("회원번호를 입력하세요: ");
            return sc.nextInt();
        }

    /* 설명. 사용자로부터 회원번호를 제외한 정보를 입력받아(회원가입용 정보) Member 타입으로 반환하는 메소드(parsing 및 가공처리) */
    private static Member signUp() {
        Member newInfo = null;
            Scanner sc = new Scanner(System.in);

        System.out.print("아이디를 입력하세요: ");
        String id = sc.nextLine();

        System.out.print("패스워드를 입력하세요: ");
        String pwd = sc.nextLine();

        System.out.print("나이를 입력하세요: ");
        int age = sc.nextInt();

        System.out.print("입력할 취미 개수를 입력하세요(숫자로 1 이상): ");
        int length = sc.nextInt();
        sc.nextLine();          // Scanner 버퍼에 남아있을 엔터 제거 용도

        String[] hobbies = new String[length];
        for (int i = 0; i < hobbies.length; i++) {
            System.out.print((i + 1) + "번째 취미를 입력하세요: ");
            String input = sc.nextLine();
            hobbies[i] = input;
        }
        newInfo = new Member(id, pwd, age, hobbies);

        System.out.print("혈액형을 입력하세요(A, AB, B, O): ");
        String bloodType = sc.nextLine().toUpperCase();
        BloodType bt = null;
        switch (bloodType) {
            case "A": bt = BloodType.A;
            case "AB": bt = BloodType.AB;
            case "B": bt = BloodType.B;
            case "O": bt = BloodType.O;
        }

        /* 필기.
         *  회원으로부터 회원가입을 위한 정보를 입력받아 Member 타입 객체 하나로 가공 처리할 때 방법이 두 가지가 있다.
         *  1. 생성자 방식(장점: 한줄 코딩 가능, 단점: 따로 생성자 추가)
         *  2. setter 방식(장점: 초기화할 개수 수정 용이, 단점: 코딩 줄 수 늘어날 수 있음)
        * */
        newInfo.setBloodType(bt);

        return newInfo;
        }
    }
