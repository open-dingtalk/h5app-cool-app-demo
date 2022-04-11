import axios from 'axios'
import React from 'react'
import './App.css'
import "antd/dist/antd.min.css"
import { Button, message, Input} from "antd"
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
            showType: 0
        }
    }

    render() {
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
                {this.state.showType === 0 && (
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
                )}
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
