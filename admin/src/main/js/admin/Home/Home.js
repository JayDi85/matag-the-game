import React, {Component} from 'react'
import Login from '../Auth/Login/Login'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'
import {connect} from 'react-redux'
import get from 'lodash/get'

class Home extends Component {
  displayLogin() {
    if (!this.props.profile.username) {
      return <Login />
    }
  }

  render() {
    return (
      <div className='page'>
        <h2>Home</h2>
        <Intro/>
        <Stats/>
        { this.displayLogin() }
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    profile: get(state, 'session.profile', {}),
  }
}

const mapDispatchToProps = dispatch => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Home)