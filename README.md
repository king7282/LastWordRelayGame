# LastWordRelayGame

자바 RMI 기술과 Socket Programming을 통해 실시간으로 게임을 진행하는 끝말잇기 게임 프로젝트 입니다.


## 사용법
LastWordGameServer측에 내부 인증서 파일이 있는데 이거 쓰기 싫으면 자신의 인증서 파일 위치를 Server의 Server.java, Client의 index.java에 있는 인증서 파일 경로를 바꿔 주시기 바랍니다.   

Server가 실행될때 자신의 IP를 표시하게 되는데 이때 여러 네트워크 드라이버가 있으면 제대로 된 IP가 맞는지 확인하고 실행시켜주시기 바랍니다.   
(안되면 네트워크 드라이버 순서 변경해서 IP를 제대로 들고오게 해주시던지 또는 직접 IP를 써도 됩니다.)

Server를 실행 시키기 위해선 argument를 port로 주시기 바랍니다(java Server 9999)   

RMI를 Server 프로젝트의 bin파일에서 실행시켜주시기 바랍니다. 이때 포트는 1099입니다. 정확한 내용은 Server의 Server.java에 RMI 키는 코드가 있으니 확인해 주시기 바랍니다. 

LastWordGameServer안에 dictonary.txt에 정답으로 인정되는 단어들의 목록을 적으시면 됩니다. 단어는 공백으로 구분됩니다.

## 실행

![대기실](https://user-images.githubusercontent.com/25100166/106236961-3108f900-6241-11eb-8e17-b6941410a1f0.png)   
대기실 찾기

![게임 방](https://user-images.githubusercontent.com/25100166/106236959-30706280-6241-11eb-9488-d6840d3ce906.png)   
게임방에서 대기중

![게임](https://user-images.githubusercontent.com/25100166/106236957-2f3f3580-6241-11eb-82ed-198b1be2d697.png)   
게임중 
