import Announcement from '../components/Announcement';
import Schedule from '../components/Schedule';
import App from '../App';
import React from 'react';
import {Router,Route,Switch,Redirect} from 'react-router-dom';
import { createHashHistory } from "history";
const history = createHashHistory();

class RouterConfig extends React.Component{
    render(){
        return(
            <Router history={history}>
                <Switch>
                    <Route path='/' exact render={()=>(
                        <Redirect to='/App'/>
                    )}/>
                    <Route path='/App' component={App}/>
                    <Route path='/Announcement' component={Announcement}/>
                    <Route path='/Schedule' component={Schedule}/>
                </Switch>
            </Router>
        )
    }
}
export default RouterConfig;