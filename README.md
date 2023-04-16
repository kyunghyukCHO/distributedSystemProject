협동분산시스템 프로젝트
====

1. 프로젝트 계획 정보
* 목표 - 클라이언트/서버 파일 동기화 시스템
* 진행방법 - 1인 프로젝트
* 개발 환경 요구사항
    * IntelliJ IDEA
    * JAVA
    * 라이브러리 및 프레임워크

2. 프로젝트 구성 (초안) 
* ONE Server and SEVERAL Clients can connect concurrently
* Server and Client connect using SOCKET 
   * Server have THREAD POOL having 10 THREADS
   * if one client try to connect server, server assign one thread from pool 
