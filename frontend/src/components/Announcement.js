import react, { useEffect, useState } from "react"
import {Form, Input, Button, message, Select, Image} from "antd"
import React from "react"
import {NavLink} from 'react-router-dom'
import '../App.css'
import "antd/dist/antd.min.css"
import axios from "axios";

const Announcement = (props) => {
    const { Option } = Select;
    const [form] = Form.useForm()
    const [cid, setCid] = useState('');
    const [cidOption, setCidOption] = useState('');
    const [conArr, setConArr] = useState([]);
    const [img, setImg] = useState('https://gw.alicdn.com/imgextra/i2/O1CN015ODkeA1wO4lFrPogt_!!6000000006297-2-tps-572-844.png');
    const domain = window.location.protocol + "//" + window.location.hostname;
    const initValue = {
        title: "公告",
        content: "大家按照这个格式填写下，每周我会做一个统计和公布哈，和大家同步下我们的进展",
        imgArr: [
            "https://gw.alicdn.com/imgextra/i1/O1CN01RX9taL1bnAWsG3tWy_!!6000000003509-2-tps-572-844.png",
            "https://gw.alicdn.com/imgextra/i2/O1CN015ODkeA1wO4lFrPogt_!!6000000006297-2-tps-572-844.png",
            "https://gw.alicdn.com/imgextra/i2/O1CN01Eq7BfJ1D4hIJ2L0RS_!!6000000000163-2-tps-572-844.png",
            "https://gw.alicdn.com/imgextra/i1/O1CN010iPftK1s0wI0kMHPI_!!6000000005705-2-tps-572-844.png"
        ]
    }

    const onSubmit = (data) => {
        data.cid = cid !== '' ? cid : cidOption;
        data.img = img;
        data.domain = domain;
        console.log("======= pushAnnouncement =======")
        axios.post( "/biz/pushAnnouncement", data).then(res => {
        }).catch(error => {
            alert("pushAnnouncement err, " + JSON.stringify(error))
        })
    }

    const selectCid = (value) => {
        let cid = encodeURIComponent(value);
        setCidOption(cid);
    }

    const selectImg = (value) => {
        const i = parseInt(value)
        const img = initValue.imgArr[i];
        setImg(img);
    }

    useEffect(()=>{
        const hash = window.location.hash
        let showGroup = true;
        if(hash){
            let indexOf = hash.indexOf("/");
            let str = hash.substr(indexOf + 1);
            let index = str.indexOf("/");
            if(index > 0){
                let cid = str.substr(index + 1);
                setCid(cid);
                showGroup = false;
            }
        }
        if(showGroup){
            axios.get("/getConversationIdMap").then(res => {
                const conversationIdMap = res.data;
                let arr = [];
                Object.keys(conversationIdMap).map((item, i) => {
                    let obj = {label: conversationIdMap[item], value:item};
                    arr.push(obj);
                })
                setConArr(arr);
            }).catch(error => {
                alert("getConversationIdMap err, " + JSON.stringify(error))
            })
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
                    <Form.Item label="公告配图" name="img">
                        <Select defaultValue="1" onChange={selectImg}>
                            <Option value="0">图1</Option>
                            <Option value="1">图2</Option>
                            <Option value="2">图3</Option>
                            <Option value="3">图4</Option>
                        </Select>
                        <br/>
                        <Image src={img} />
                    </Form.Item>

                    {(cid === '' && conArr.length > 0) && (
                        <Form.Item label="选择群组" name="cid">
                        <Select options={conArr} onSelect={selectCid}/>
                        </Form.Item>
                    )}
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
