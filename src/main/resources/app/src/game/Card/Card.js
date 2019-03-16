import React, {PureComponent} from 'react'
import {get} from 'lodash'
import CardUtils from './CardUtils'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class Card extends PureComponent {
  constructor(props) {
    super(props);
    this.onWheel= this.onWheel.bind(this);
  }

  name() {
    return get(this.props.cardInstance, 'card.name', 'card')
  }

  imageUrl() {
    const name = CardUtils.normalizeCardName(this.name())
    if (name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("/img/cards/${name}.jpg")`
    }
  }

  getClasses() {
    let classes = 'card'

    if (get(this.props.cardInstance, 'modifiers.tapped'))  {
      classes += ' ' + this.props.cardInstance.modifiers.tapped.toLowerCase()
    }

    if (get(this.props.cardInstance, 'modifiers.summoningSickness')) {
      classes += ' summoning-sickness'
    }

    if (get(this.props.cardInstance, 'modifiers.attacking')) {
      classes += ' attacking'
    }

    return classes
  }

  onWheel(e) {
    const name = CardUtils.normalizeCardName(this.name())
    if (name !== 'card') {
      if (e.deltaY < 0) {
        this.props.maximizeCard(this.imageUrl())
      }
    }
  }

  render() {
    if (this.props.cardInstance) {
      return <div id={'card-' + this.props.cardInstance.id}
                  className={this.getClasses()}
                  style={{backgroundImage: this.imageUrl(), ...this.props.style}}
                  onClick={this.props.onclick}
                  onWheel={this.onWheel}/>
    } else {
      return <div className='card' style={{backgroundImage: this.imageUrl(), ...this.props.style}} />
    }
  }
}

const maximizeCardEvent = (cardImage) => {
  return {
    type: 'MAXIMIZE_MINIMIZE_CARD',
    value: {
      cardImage: cardImage
    }
  }
}

const mapStateToProps = state => {
  return {
    message: get(state, 'message', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    maximizeCard: bindActionCreators(maximizeCardEvent, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Card)