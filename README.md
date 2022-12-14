# 1. 천사소녀 네티

## 목차

### 1부 네티 소개

- #### 1장) 네티 맛보기
  ```
  간단하게 server, client를 생성하여 사용 해본다.
  ```
- #### 2장) 네티의 주요 특징

  ```
  네트워크 입출력 방법인 동기와 비동기 그리고 블로킹과 논블로킹의 차이점을 이해하고
  네티가 제공하는 이벤트 기반 프로그래밍의 원리를 이해한다.
  
  동기화 : 서로 영향을 주고받거나 받게 됨으로써 동일한 주기를 갖게 되는것
  비동기화 : 송수신 간에 신호를 주고 받으며 작업을 수행하는 방식,
           시작/정지, 단계적 시작 정지 방식이 있음 
  
  블로킹 : 요청한 작업이 성공하거나 에러가 발생하기 전까지 응답을 돌려주지 않는 것
  논블로킹 :  요청한 작업의 성공 여부와 상관없이 바로 결과를 돌려주는 것
  ```

### 2부 네티 상세

- #### 3장) 부트스트랩
  ```
  정의 : 네티로 작성한 네트워크 애플리케이션의 동작 방식과 환경을 설정하는 도우미 클래스.
  대표적인 구성 : [이벤트루프(스레드 구현), 소켓의 모드, 파이프라인]
  구조 : 
        전송계층(소켓모드, IO종류),
        이벤트루프(단일,다중스레드), 
        파이프라인, 
        소켓주소포트, 
        소켓 옵션
  메서드 : 
        **ServerBootStrap**
        group : 이벤트 루프 설정
        channel : 소켓 입출력 모드 설정
        channelFactory : 소켓 입출력 모드 설정
        handler : 서버 소켓 채널의 이벤트 핸들러 설정
        childHandler : 소켓 채널의 데이터 가공 핸들러 설정
        option : 서버 소켓 채널의 소켓 옵션 설정
        childOption : 소켓 채널의 소켓 옵션 설정
  
        **BootStrap**
        group : 이벤트 루프 설정
        channel : 소켓 입출력 모드 설정
        channelFacktory : 소켓 입출력 모드 설정
        handler : 클라이언트 소켓 채널의 이벤트 핸들러 설정
  ```
  - #### 4장) 채널 파이프라인과 코덱
  ```
  **채널 파이프라인**
  정의 : 채널에서 발생한 이벤트가 이동하는 통로
  
  Handler : 파이프라인을 통해서 이동하는 이벤트를 처리하는 클래스
  Codec : 이벤트 핸들러를 상속받아서 구현한 구현체
  
  *InboundEvent : 소켓 채널에서 발생한 이벤트 중에서 연결 상대방이 어떤 동작을 취했을 때 발생한다.
                (ex. 채널 활성화, 데이터 수신 등의 이벤트)
  순서 : channelRegistered (ServerSocketChannel을 포함한다) 
        -> channelActive (SocketChannel 등록감지)
        -> channelRead (데이터 수신, 단! 버퍼크기를 넘어가는 경우 여러번 실행)
        -> channelReadComplete (데이터가 모두 수신되었을때 실행)
        -> channelInactive (채널이 비활성화 될때 감지)
        -> channelUnregistered (채널이 이벤트 루프에서 제거되었을때 실행)
  
  *OutboundEvent : 소켓 채널에서 발생한 이벤트 중에서 네티 사용자가 요청한 동작에 해당하는 이벤트를 말한다.
                (ex. 연결 요청, 데이터 전송, 소켓 닫기 등의 이벤트)
  종류 : bind (서버 소켓 채널이 클라이언트의 연결을 대기하는 IP와 포트가 설정 되었을대 실행)
        connect (클라이언트 소켓 채널이 서버에 연결 될때 실행)
        disconnect (소켓 채널이 끊어졌을때 실행)
        close (소켓 채널의 연결이 닫혔을때 실행)
        write (소켓 채널에 데이터가 기록 되었을대 실행, 데이터 버퍼가 인수로 사용됨)
  ```
  
  - #### 5장) 이벤트 모델
  ```
  **EventLoop**
  개념 : 이벤트큐에 접근해서 이벤트를 실행하기 위한 무한루프 스레드
  구분 : 
      1. 스레드 종류에 따른 분류 : 단일 스레드 이벤트 루프, 다중 스레드 이벤트 루프
      2. 결과 리턴방식에 따른 분류 : 콜백패턴, 퓨처패턴
  ```
  - #### 6장) 바이트 버퍼

### 3부 네티 응용

  - #### 7장) 네티와 채널 보안
  - #### 8장) 네티와 서드파티 연동
  - #### 9장) **실전예제** 네티로 구현한 API서버
