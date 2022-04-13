import react, { useEffect, useState } from "react"
import { Form, Input, Button, message } from "antd"
import React from "react"
import { DatePicker } from "antd-mobile"
import moment from "moment"
import {NavLink} from 'react-router-dom'
import '../App.css'
import "antd/dist/antd.min.css"
import axios from "axios"
import * as dd from "dingtalk-jsapi"

const Schedule = (props) => {
    const { TextArea } = Input;
    const [form] = Form.useForm();
    const [pickerV, setPickerV] = useState(false);
    const [users, setUsers] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [sign, setSign] = useState({});
    const [cid, setCid] = useState('');

    const initValue = {
        title: "日程主题",
        scheduleTime: new Date(),
        address: "日程地点",
    }

    const onSubmit = (data) => {
        const formData ={
            ...data,
            scheduleTime: moment(data.scheduleTime).format("YYYY-MM-DD HH:mm:ss")
        }
        syncSchedule(formData)
    }

    const syncSchedule = (data) => {
        const { title, scheduleTime, address} = data
        axios.post("/biz/syncSchedule", {
            title:title,
            date:scheduleTime,
            address:address,
            users:users,
            departments:departments,
            cid:cid
        }).then(res => {
        }).catch(error => {
            alert("syncSchedule err, " + JSON.stringify(error))
        })
    }

    const pickerFn = (departments = [], users = []) => {
        let str = "";
        if(departments.length > 0) {
            str += '部门：' + departments.map((item, i) => (
                item.name + ','
            ))
            str = str.substr(0, str.length - 1) + '\n';
        }
        if(users.length > 0) {
            str += '人员：' + users.map((item, i) => (
                item.name + ','
            ))
            str = str.substr(0, str.length - 1)
        }
        form.setFieldsValue({
            users:str
        })
    }

    const config = () => {
        let url = window.location.href;
        message.info("url: " + url);
        axios.post("/sign?url=" + url
        ).then(res => {
            setSign(res.data);
            ddConfig(res.data);
        }).catch(error => {
            alert("sign err, " + JSON.stringify(error))
        })
    }

    const ddConfig = (data) => {
        dd.config({
            agentId: data.agentId, // 必填，微应用ID
            corpId: data.corpId,//必填，企业ID
            timeStamp: data.timeStamp, // 必填，生成签名的时间戳
            nonceStr: data.nonceStr, // 必填，自定义固定字符串。
            signature: data.signature, // 必填，签名
            type: data.type,   //选填。0表示微应用的jsapi,1表示服务窗的jsapi；不填默认为0。该参数从dingtalk.js的0.8.3版本开始支持
            jsApiList : [
                'biz.contact.complexPicker',
            ] // 必填，需要使用的jsapi列表，注意：不要带dd。
        });
        dd.error(function (err) {
            alert('dd error: ' + JSON.stringify(err));
        })
    }

    useEffect(()=>{
        let hash = window.location.hash
        if(hash){
            let indexOf = hash.indexOf("/");
            let str = hash.substr(indexOf + 1);
            let index = str.indexOf("/");
            if(index > 0) {
                let cid = str.substr(index + 1);
                message.info("cid : " + cid);
                setCid(cid);
            }
        };
        let href = window.location.href;
        message.info("href: " + href);
        axios.post("/sign?url=" + href
        ).then(res => {
            setSign(res.data);
            ddConfig(res.data);
        }).catch(error => {
            alert("sign err, " + JSON.stringify(error))
        })
    },[])


    const complexPicker = (e) => {

        dd.biz.contact.complexPicker({
            title:"选择人员",            //标题
            corpId:sign.corpId,              //企业的corpId
            multiple:true,            //是否多选
            limitTips:"超出了",          //超过限定人数返回提示
            maxUsers:10,            //最大可选人数
            pickedUsers:[],            //已选用户
            pickedDepartments:[],          //已选部门
            disabledUsers:[],            //不可选用户
            disabledDepartments:[],        //不可选部门
            requiredUsers:[],            //必选用户（不可取消选中状态）
            requiredDepartments:[],        //必选部门（不可取消选中状态）
            appId:sign.agentId,              //微应用Id，企业内部应用查看AgentId
            permissionType:"",          //可添加权限校验，选人权限，目前只有GLOBAL这个参数
            responseUserOnly:false,        //返回人，或者返回人和部门
            startWithDepartmentId:0 ,   //仅支持0和-1
            onSuccess: function(result) {
                setUsers(result.users);
                setDepartments(result.departments);
                pickerFn(result.departments, result.users);
            },
            onFail : function(err) {
                alert("complexPicker err, " + JSON.stringify(err))
            }
        });
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
                <h3 className="title">酷应用示例—同步日程到群</h3>
                <Form form={form} onFinish={onSubmit} initialValues={initValue}>
                    <Form.Item label="日程主题" name="title">
                        <Input placeholder="请输入日程主题" />
                    </Form.Item>
                    <Form.Item label="日程地点" name="address">
                        <Input placeholder="请输入日程地点" />
                    </Form.Item>
                    <Form.Item label="日程人员" name="users">
                        <TextArea placeholder="请选择日程参与人员" readOnly={true} />
                    </Form.Item>
                    <Form.Item label=''>
                        <Button onClick={(e) => complexPicker(e)}>
                            选择组织人员
                        </Button>
                    </Form.Item>


                    <Form.Item label="日程时间" name="scheduleTime">
                        <DatePicker
                            visible={pickerV}
                            onClose={() => {
                                setPickerV(false)
                            }}
                            min={new Date()}
                            precision="second"
                            onConfirm={(val, s) => {
                                form.setFieldsValue({
                                    scheduleTime: val,
                                })
                            }}
                        >
                        {(value) => (
                            <div style={{ textAlign: "left" }}>
                                <Button onClick={() => setPickerV(true)} >
                                    选择时间
                                </Button>{" "}
                                {moment(value).format("YYYY-MM-DD HH:mm:ss")}
                            </div>
                        )}
                        </DatePicker>
                    </Form.Item>
                    <Button htmlType="submit" type="primary">
                        提交
                    </Button>
                </Form>
                <NavLink to='/App'>←应用首页</NavLink>
            </div>
        </div>
    )
}

export default Schedule
