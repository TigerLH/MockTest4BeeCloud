if [ -z "$1" ]; then
   echo "Usage error exit"
   echo "./build.sh [war] [ver]"
exit 2
fi
if [ -z "$2" ];  then
   echo "Usage error please input docker version . exit"
   echo "./build.sh [war] [ver]"
exit 2
fi
unzip $1 -d ./beecloud
docker build -t "com.beecloud.mockserver:$2" .
rm -rf ./beecloud 
