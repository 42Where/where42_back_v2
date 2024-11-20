# 가동중인 app 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=app" | grep -q . && docker stop app && docker rm app | true

sudo docker ps -a -q --filter "name=promtail" | grep -q . && docker stop promtail && docker rm promtail || true

# 기존 이미지 삭제
sudo docker rmi where42/where42:latest

sudo echo $PASSWORD | docker login -u $USERNAME --password-stdin

# 도커허브 이미지 pull
sudo docker pull where42/where42:latest

# 도커 run
docker run -d -p 8080:8080 -v /home/ec2-user:/config --name app where42/where42:latest -e TZ=Asia/Seoul

docker run -d \
 --name promtail \
 -v /home/ec2-user/logs:/logs \
 -v /home/ec2-user/promtail-config.yml:/etc/promtail/config.yml \
 grafana/promtail:latest \
 -config.file=/etc/promtail/config.yml

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않습니다.
docker rmi -f $(docker images -f "dangling=true" -q) || true

docker system prune -af