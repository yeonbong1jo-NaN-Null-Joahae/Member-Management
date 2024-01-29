package manage.assignment.repository;

import manage.assignment.aggregate.BloodType;
import manage.assignment.aggregate.Member;
import manage.assignment.stream.MyObjectOutput;

import java.io.*;
import java.util.ArrayList;

/* 설명. 데이터와 입출력을 위해 만들어지며 성공/실패 결과를 반환하는 클래스 */
public class MemberRepository {

    /* 설명. nextNum을 초기값 1로 필드 선언 및 Member리스트의 memNo에 nextNum++ 추가 */
    private ArrayList<Member> memberList = new ArrayList<>();
    private int nextNum = 1;

    /* 설명. 프로그램이 켜지자마자(MemberRepository()가 실행되자마자) 파일에 dummy 데이터 추가 및 입력받기 */
    public MemberRepository() {

        /* 설명. 회원가입 기능 추가 후 이제는 파일이 기존에 존재하면(처음이 아니므로) 회원 3명으로 초기화 하기를 하지 않는다. */
        File file = new File("java-team-practice/src/main/java/mane/assignment/db/memberDB.dat");
        if (!file.exists()) {
            ArrayList<Member> members = new ArrayList<>();
            members.add(new Member(nextNum++, "user01", "pass01", 20, new String[]{"발레", "수영"}, BloodType.A));
            members.add(new Member(nextNum++, "user02", "pass02", 10, new String[]{"게임", "영화시청"}, BloodType.B));
            members.add(new Member(nextNum++, "user03", "pass03", 15, new String[]{"음악감상", "독서", "명상"}, BloodType.O));
            saveMembers(members);
        }

        loadMembers();

//        System.out.println("==== repository에서 회원정보 다 읽어왔는지 확인 ====");       // 나중엔 다 지우기
//        for(Member m: memberList) {
//            System.out.println(m);
//        }
    }

    public ArrayList<Member> getMemberList() {
        return memberList;
    }

    /* 설명. 회원이 담긴 ArrayList를 던지면 파일에 출력하는 기능 */
    private void saveMembers(ArrayList<Member> members) {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("java-team-practice/src/main/java/manage/assignment/db/memberDB.dat")));

            /* 설명. 넘어온 회원 수만큼 객체 출력하기 */
            for (Member m: members) {
                oos.writeObject(m);
            }

            oos.flush();        // 출력 시에는 flush 해줄 것.

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(oos != null) oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* 설명. 파일로부터 회원 객체들을 입력받아 memberList에 쌓기 */
    private void loadMembers() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("java-team-practice/src/main/java/manage/assignment/db/memberDB.dat")));

            /* 설명. 파일로부터 모든 회원 정보를 읽어 memberList에 추가(add) */
            while (true) {
                memberList.add((Member) (ois.readObject()));
            }

        } catch (EOFException e) {
            System.out.println("회원 정보 모두 로딩됨...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Member> selectAllMembers() {
        return memberList;
    }

    public Member selectMember(int memNo) {
        for(Member m: memberList) {
            if(m.getMemNo() == memNo) return m;
        }
        return null;
    }

    public int selectLastMemberNo() {
        return memberList.get(memberList.size() - 1)        // 가장 최근에 가입한 회원
                .getMemNo();                       // 그 회원의 회원번호
    }

    /* 설명. 기존 회원(객체)에 이어서 파일 출력을 하고 추가한 객체의 수를 반환(feat. DML 작업의 결과는 int) */
    /* 설명. 객체 출력을 할 예정인데 기존 ObjectOutputStream 대신 새로 정의한 스트림으로 회원 한명 파일 출력해서 int 반환하기(feat. 이어쓰기) */
    public int registMember(Member member) {
        MyObjectOutput moo = null;
        try {
            moo = new MyObjectOutput(
                    new BufferedOutputStream(
                            new FileOutputStream("java-team-practice/src/main/java/manage/assignment/db/memberDB.dat", true)));

            /* 설명. setMemNo(memNo)에 nextNum 대입 */
            member.setMemNo(nextNum);
            /* 설명. 파일로 객체 하나 출력하기 */
            moo.writeObject(member);

            /* 설명. repository의 memberList에도 추가 */

            /* 설명. memberList의 nextNum을(memNo) 1씩 추가해줌 */
            memberList.add(member);
            nextNum++;

            moo.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (moo != null) moo.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return 1;
    }

    public int deleteMember(int memNo) {
        for (int i = 0; i < memberList.size(); i++) {
            if(memberList.get(i).getMemNo() == memNo) {     // 지우려는 회원과 같은 회원 번호인 index 찾기

                /* 설명. 프로그램 상에서 회원을 관리하는 memberList 뿐 아니라 DB(회원 파일)도 같이 적용되게 함 */
                memberList.remove(i);
                saveMembers(memberList);
                return 1;
            }
        }
        return 0;
    }


    /* 설명. id를 통해 멤버를 검색하는 메소드 */
    public Member findMember(String id){
        for(Member m: memberList) {
            if(m.getId().equals(id)) return m;
        }
        return null;
    }

    /* 설명. case 6.에 추가할 로그인 기능의 로그인 여부(loginCheck)를 확인하는 메소드 */
    public int loginCheck(Member m, String pwd){
       if(m == null){
           return 1;        // ID 없음
       } else {
           if(m.getPwd().equals(pwd)){
               return 2;        // 로그인 성공
           } else{
               return 3;        // 비번 틀림
           }
       }
    }

    public int modifyMember(Member member) {
        MyObjectOutput moo = null;
        try {
            moo = new MyObjectOutput(
                    new BufferedOutputStream(
                            new FileOutputStream("java-team-practice/src/main/java/manage/assignment/db/memberDB.dat", true)));

            // 알고리즘 정리
            // 1. memberList 에서 기존 member를 지우고
            // 2. 지운 member 의 위치에 바뀐 정보가 들어있는 member 를 넣는다.
            // 3. 그리고 saveMembers(memberList)

            for (int i = 0; i < memberList.size(); i++) {
                if (memberList.get(i).getMemNo() == member.getMemNo()) {
                    memberList.remove(memberList.get(i));
                    memberList.add(i, member);
                    saveMembers(memberList);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (moo != null) moo.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return 1;
    }
    /* 설명. searchMemberByHobby 메소드 추가 */
    public ArrayList<Member> searchMemberByHobby(String hobby) {
        /* 메모
            1. MemberService에서 hobby를 매개변수로 받아옴
            2. 취미가 동일한 멤버 객체를 담을 ArrayList를 생성
            3. forEach문을 사용하여 Member 객체의 hobby 필드(String 배열)에서 전달받은 매개변수 hobby와 일치하는 값이 있다면
               해당 Member 객체를 ArrayList에 저장
            4. ArrayList를 MemberService로 반환
         */

        ArrayList<Member> sharedHobbyMembers = new ArrayList<>();

        for(Member m : memberList){                     // memberList에 저장된 값 순차 탐색
            for(String s : m.getHobbies()){             // MemberList에 저장된 member 객체의 취미 배열 순차 탐색
                if(s.equals(hobby)){       // 사용자가 입력한 취미와 배열의 값이 일치한다면
                    sharedHobbyMembers.add(m);          // 해당 멤버 객체를 sharedHobbyMembers에 저장
                }
            }
        }
        return sharedHobbyMembers;                      // 입력된 값과 일치하는 취미를 가진 멤버 객체 리스트 반환
    }
}
