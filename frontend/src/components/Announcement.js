import react, { useEffect, useState } from "react"
import {Form, Input, Button, message} from "antd"
import React from "react"
import {NavLink} from 'react-router-dom'
import '../App.css'
import "antd/dist/antd.min.css"
import axios from "axios";

const Announcement = (props) => {
    const [form] = Form.useForm()
    const [cid, setCid] = useState('');
    const initValue = {
        title: "公告标题",
        content: "公告内容",
    }

    const onSubmit = (data) => {
        data.cid = cid;
        console.log("======= pushAnnouncement =======")
        axios.post( "/biz/pushAnnouncement", data).then(res => {
        }).catch(error => {
            alert("pushAnnouncement err, " + JSON.stringify(error))
        })
    }

    useEffect(()=>{
        let hash = window.location.hash
        if(hash){
            let indexOf = hash.indexOf("/");
            let str = hash.substr(indexOf + 1);
            let index = str.indexOf("/");
            if(index > 0){
                let cid = str.substr(index + 1);
                message.info("cid : " + cid);
                setCid(cid);
            }
        }
    },[])


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
                <h3 className="title">酷应用示例—推送公告到群</h3>
                <Form form={form} onFinish={onSubmit} initialValues={initValue}>
                    <Form.Item label="公告标题" name="title">
                        <Input placeholder="请输入公告标题"/>
                    </Form.Item>
                    <Form.Item label="公告内容" name="content">
                        <Input placeholder="请输入公告内容"/>
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

export default Announcement
