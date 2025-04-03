#/bin/bash
set -e
version=2.3.7
docker build -t orion-visor-adminer:${version} .
docker tag orion-visor-adminer:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version}
docker tag orion-visor-adminer:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:latest
