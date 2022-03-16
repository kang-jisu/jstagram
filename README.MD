## 인스타그램 스프링 부트로 구현하기프로젝트 

### github
- [jstagram](https://github.com/kang-jisu/jstagram)

### 프로젝트 목표 및 후기
- 유저,게시판,댓글 기능을 구현하며 기본적인 Spring Boot의 특징과 Spring MVC 구조를 익히기 위한 토이 프로젝트입니다.
- 추가적으로 API를 구현하고 React와 연동하는 과정에서 Cors에러를 겪으며 도메인이 다른 요청을 웹에서 제한한다는 것을 알게되었습니다.
- 로컬환경이 아닌 클라우드 환경에서 서버에 실제로 처음 배포해보는 경험이었습니다. EC2 인스턴스 생성 방법, nohup을 이용한 백그라운드 실행을 경험하였습니다.


### 프로젝트 기간
2019.10 

### 사용 기술
spring boot, gradle, thymeleaf, spring data jpa, spring security, aws s3, aws ec2, mysql, bootstrap

### 구현 기능
[컨트롤러 명세](https://github.com/kang-jisu/jstagram/blob/master/etc/%EC%BB%A8%ED%8A%B8%EB%A1%A4%EB%9F%AC%EB%AA%85%EC%84%B8.pdf)  
*rest한 api 작성법, API docs 작성에 대해 알지 못할 때 만든 명세서이지만 최대한 구현한 기능에 대하여 정리하려고 하였습니다.*
- 회원
  - 회원 가입, 회원 탈퇴
  - 로그인, 로그아웃 
  - 프로필 보기
    - 쓴 글 보기
  - 프로필 수정
  - 팔로우
  - 로그인 여부에 따른 처리 
- 게시글(피드)(로그인/비로그인, 작성자만 수정,삭제 가능)
  - 작성
    - DB에 글 등록, S3에 사진 업로드
  - 수정 
  - 삭제 
  - 목록 조회
  - 상세 게시글 보기
- 댓글
  - 댓글 달기
  - 댓글 삭제

### 모델 
<img width="609" alt="스크린샷 2022-03-07 오후 6 31 38" src="https://user-images.githubusercontent.com/37580216/157004734-9dfa963f-9a80-4f50-8ff5-b46a4780bf38.png">


### 화면
<img width="300" alt="jstagram_detail" src="https://user-images.githubusercontent.com/37580216/157003170-fc3422b5-41a0-49f1-8108-b8fd55a2b2eb.png">
<img width="300" alt="jstagram_main" src="https://user-images.githubusercontent.com/37580216/157003184-662a3c10-83d7-4bc0-b818-35f476454777.png">
<img width="300" alt="jstagram_profile" src="https://user-images.githubusercontent.com/37580216/157003195-bd78e3a3-d5a4-4a35-a16a-3dd891085d81.png">

