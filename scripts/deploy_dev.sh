#실행중인 docker container 다운 및 삭제
sudo docker-compose -f ./docker-compose.yml down
sudo docker-compose -f ./docker-compose.yml down --volumes
sudo docker system prune -af

#dockerHub 로그인
sudo echo $PASSWORD | docker login -u $USERNAME --password-stdin

#docker container 업
sudo docker-compose -f ./docker-compose.yml up -d

#사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않습니다.
sudo docker rmi -f $(docker images -f "dangling=true" -q) || true
sudo docker system prune -af





