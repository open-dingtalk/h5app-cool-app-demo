import react, { useEffect, useState } from "react"
import { Form, Input, Button } from "antd"
import React from "react"
import { DatePicker } from "antd-mobile"
import moment from "moment"
import {NavLink} from 'react-router-dom'
import '../App.css'
import "antd/dist/antd.min.css"
import axios from "axios"

const Schedule = (props) => {
    const [form] = Form.useForm();
    const [pickerV, setPickerV] = useState(false);
    const initValue = {
        title: "日程主题",
        date: "日程时间",
        address: "日程地点",
    }

    const onSubmit = (data) => {
        const formData ={
            ...data,
            date: moment(data.date).format("YYYY-MM-DD HH:mm:ss")
        }
        // props.onClick(formData)
        syncSchedule(formData)
    }

    const syncSchedule = (data) => {
        const { title, date, address} = data
        axios.post(this.state.domain + "/biz/syncSchedule", {
            title:title,
            date:date,
            address:address
        }).then(res => {

        }).catch(error => {
            alert("syncSchedule err, " + JSON.stringify(error))
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
                <h4 className="title">酷应用示例—同步日程到群</h4>
                <Form form={form} onFinish={onSubmit} initialValues={initValue}>
                    <Form.Item label="日程主题" name="title">
                        <Input placeholder="请输入日程主题" />
                    </Form.Item>
                    <Form.Item label="日程地点" name="content">
                        <Input placeholder="请输入日程地点" />
                    </Form.Item>
                    {/*<Form.Item label="日程时间" name="date">*/}
                    {/*    <DatePicker*/}
                    {/*        visible={pickerV}*/}
                    {/*        onClose={() => {*/}
                    {/*            setPickerV(false)*/}
                    {/*        }}*/}
                    {/*        min={new Date()}*/}
                    {/*        precision="second"*/}
                    {/*        onConfirm={(val, s) => {*/}
                    {/*            form.setFieldsValue({*/}
                    {/*                date: val,*/}
                    {/*            })*/}
                    {/*        }}*/}
                    {/*    >*/}
                    {/*    {(value) => (*/}
                    {/*        <div style={{ textAlign: "left" }}>*/}
                    {/*            <Button onClick={() => setPickerV(true)} type="primary">*/}
                    {/*                选择日程时间*/}
                    {/*            </Button>{" "}*/}
                    {/*            {moment(value).format("YYYY-MM-DD HH:mm:ss")}*/}
                    {/*        </div>*/}
                    {/*    )}*/}
                    {/*    </DatePicker>*/}
                    {/*</Form.Item>*/}
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
