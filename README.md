# JanusWebrtc

1. 安装janus https://github.com/meetecho/janus-gateway
2. 安装turn服务 http://quanzhan.applemei.com/webStack/TlRZMU1nPT0=

3. 更改janus配置 janus.jcfg 找到nat 改为下面配置
stun_server改为127.0.0.1(安装stun服务机器的IP地址)

4. 把JunusClientUtil里的server_uri改为安装janus机器的ip地址

5. 启动访问
1) localhost:8080/index videocall页面
2) localhost:8080/second videoroom大厅页面
