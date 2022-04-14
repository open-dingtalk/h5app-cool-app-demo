import axios from 'axios'
import React from 'react'
import './App.css'
import "antd/dist/antd.min.css"
import { Button, message, Carousel, Timeline} from "antd"
import * as dd from "dingtalk-jsapi"
import Announcement from "./components/Announcement"
import Schedule from "./components/Schedule"
import {NavLink} from 'react-router-dom'

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            //内网穿透工具介绍:
            // https://developers.dingtalk.com/document/resourcedownload/http-intranet-penetration?pnamespace=app
            domain: "",
            corpId: '',
            authCode: '',
            userId: '',
            userName: '',
            showType: 0,
            log:[]

        }
    }

    constructorContentStyle(url) {
        const contentStyle = {
            height: '160px',
            color: '#fff',
            lineHeight: '160px',
            textAlign: 'center',
            background: '#364d79',
            backgroundImage: 'url(' + url + ')'
        }
        return contentStyle;
    }

    getLog() {
        setInterval(async ()=>{
            axios.get(this.state.domain + "/getLog")
                .then(res => {
                    if (res.data) {
                        const logList = res.data;
                        this.setState({
                            log: logList
                        });
                    }
                }).catch(error => {
                alert("getLog err, " + JSON.stringify(error))
            })
        },10000)
    }

    render() {
        this.getLog();
        if (this.state.userId === '') {
            // 免登操作
            this.login();
        }
        return (
        <div className="content">
            <div className="header">
                <img
                    src="https://img.alicdn.com/imgextra/i3/O1CN01Mpftes1gwqxuL0ZQE_!!6000000004207-2-tps-240-240.png"
                    className="headImg"
                />
                钉钉模板
            </div>
            <div className="App">
                <h2>酷应用配置</h2>
                <p>体验此应用需要做如下配置，参考文档：https://open.dingtalk.com/document/org/group-application</p>
                <Carousel autoplay={true}>
                    <div>
                        <p style={{
                            height: '160px',
                            color: 'red',
                            lineHeight: '160px',
                            textAlign: 'center',
                            background: '#364d79',
                            backgroundRepeat:'no-repeat',
                            backgroundSize:'100% 100%',
                            backgroundImage: 'url(https://img.alicdn.com/imgextra/i2/O1CN01MG0GBf1fbxJHpo96o_!!6000000004026-2-tps-988-593.png)'
                        }}>第一步：配置机器人</p>
                    </div>
                    <div>
                        <p style={{
                            height: '160px',
                            color: 'red',
                            lineHeight: '160px',
                            textAlign: 'center',
                            background: '#364d79',
                            backgroundRepeat:'no-repeat',
                            backgroundSize:'100% 100%',
                            backgroundImage: 'url(https://img.alicdn.com/imgextra/i4/O1CN01DXlDeL1ZBx81BluOv_!!6000000003157-2-tps-1013-610.png)'
                        }}>第二步：开启配置应用扩展</p>
                    </div>
                    <div>
                        <p style={{
                            height: '160px',
                            color: 'red',
                            lineHeight: '160px',
                            textAlign: 'center',
                            background: '#364d79',
                            backgroundRepeat:'no-repeat',
                            backgroundSize:'100% 100%',
                            backgroundImage: 'url(https://img.alicdn.com/imgextra/i3/O1CN01Lz3wjC1aFRlgZo0fW_!!6000000003300-2-tps-1005-536.png)'
                        }}>第三步：配置群入口</p>
                    </div>
                    <div>
                        <p style={{
                            height: '160px',
                            color: 'red',
                            lineHeight: '160px',
                            textAlign: 'center',
                            background: '#364d79',
                            backgroundRepeat:'no-repeat',
                            backgroundSize:'100% 100%',
                            backgroundImage: 'url(https://img.alicdn.com/imgextra/i4/O1CN01DXlDeL1ZBx81BluOv_!!6000000003157-2-tps-1013-610.png)'
                        }}>第四步：点击按钮发布到群</p>
                    </div>
                </Carousel>
                <br/><br/>
                <div>
                    <h2>酷应用示例</h2>
                    <p>
                        <Button type="primary">
                            <NavLink to='/Announcement'>推送公告到群</NavLink>
                        </Button>
                    </p>
                    <p>
                        <Button type="primary">
                            <NavLink to='/Schedule'>同步日程到群</NavLink>
                        </Button>
                    </p>
                </div>
                <br/><br/>
                <div>
                    <h2>酷应用动作</h2>
                    <p>
                        {this.state.log.length > 0 && (
                            <div>
                                <Timeline>
                                    {this.state.log.map((item, i) => (
                                        <Timeline.Item key={i}>{item}</Timeline.Item>
                                    ))}
                                </Timeline>
                            </div>
                        )}
                    </p>
                </div>
            </div>
        </div>
        );
    }

    //登录-获取corpId
    login() {
        axios.get(this.state.domain + "/getCorpId")
            .then(res => {
                if (res.data) {
                    this.loginAction(res.data);
                }
            }).catch(error => {
            alert("corpId err, " + JSON.stringify(error))
        })
    }

    //登录操作
    loginAction(corpId) {
        // alert("corpId: " +  corpId);
        let _this = this;
        dd.runtime.permission.requestAuthCode({
            corpId: corpId,//企业 corpId
            onSuccess: function (res) {
                // 调用成功时回调
                _this.state.authCode = res.code
                axios.get(_this.state.domain + "/login?authCode=" + _this.state.authCode
                ).then(res => {
                    if (res && res.data.success) {
                        let userId = res.data.data.userId;
                        let userName = res.data.data.userName;
                        message.info('登录成功，你好' + userName);
                        setTimeout(function () {
                            _this.setState({
                                userId: userId,
                                userName: userName
                            })
                        }, 0)
                    } else {
                        alert("login failed --->" + JSON.stringify(res));
                    }
                }).catch(error => {
                    alert("httpRequest failed --->" + JSON.stringify(error))
                })
            },
            onFail: function (err) {
                // 调用失败时回调
                alert("requestAuthCode failed --->" + JSON.stringify(err))
            }
        });
    }

}

export default App;
