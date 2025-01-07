#/bin/bash
version=2.2.3
docker build -t orion-visor-adminer:${version} .
docker tag orion-visor-adminer:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version}
docker tag orion-visor-adminer:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:latest