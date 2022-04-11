import axios from 'axios'
import React from 'react'
import './App.css'
import "antd/dist/antd.min.css"
import { Button, message, Input} from "antd"
import * as dd from "dingtalk-jsapi"
import Announcement from "./components/Announcement"
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
        this.showPage();
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
                            <Button type="primary" onClick={() => this.setState({showType : 2})}>
                                同步日程到群
                            </Button>
                        </p>
                    </div>
                )}
                {this.state.showType === 1 && (
                    <div>
                        <Announcement
                            onClick={(e) => this.pushAnnouncement(e)}
                        />

                    </div>
                )}
            </div>
        </div>
        );
    }

    backHome(){
        this.setState({
            showType : 0
        })
    }

    writeAnnouncement(e){

    }

    pushAnnouncement(data){
        const { title, content } = data
        axios.post(this.state.domain + "/biz/pushAnnouncement", {
            title:title,
            content:content
        }).then(res => {

        }).catch(error => {
            alert("pushAnnouncement err, " + JSON.stringify(error))
        })
    }

    syncSchedule(){
        axios.post(this.state.domain + "/biz/syncSchedule")
            .then(res => {

            }).catch(error => {
            alert("pushAnnouncement err, " + JSON.stringify(error))
        })
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

    showPage(){
        // document.getElementById("root").addEventListener((ele)=>{
        //     console.log(ele.target,'======')
        // })
        // let pageType = getUrlSearchParam("pageType");
        // if(pageType){
        //     if(pageType !== this.state.showType){
        //         this.setState({
        //             showType : pageType
        //         })
        //     }
        // }
    }

}

function getUrlSearchParam(key) {
    var search = window.location.search
    var arr = !search ? [] : search.substr(1).split("&")
    var param = {}
    for (var i = 0, l = arr.length; i < l; i++) {
        var kv = arr[i].split("=")
        param[kv[0]] = kv[1]
    }
    return key ? param[key] || "" : param
}

export default App;
