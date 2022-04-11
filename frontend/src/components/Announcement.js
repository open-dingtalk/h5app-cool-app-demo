import {Form, Input, Button} from "antd"
import React from "react"
import {NavLink} from 'react-router-dom'
import '../App.css'
import "antd/dist/antd.min.css"
import axios from "axios";

const Announcement = (props) => {
    const [form] = Form.useForm()
    const initValue = {
        title: "公告标题",
        content: "公告内容",
    }

    const onSubmit = (data) => {
        // props.onClick(data)
        pushAnnouncement(data)
    }

    const pushAnnouncement = (data) => {
        const {title, content} = data
        axios.post(this.state.domain + "/biz/pushAnnouncement", {
            title: title,
            content: content
        }).then(res => {

        }).catch(error => {
            alert("pushAnnouncement err, " + JSON.stringify(error))
        })
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
                <h4 className="title">酷应用示例—推送公告到群</h4>
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
