var { requireNativeComponent, PropTypes } = require('react-native');

var iface = {
    name: 'VectorDrawableView',
    propTypes: {
        value: PropTypes.number
    }
};

module.exports = requireNativeComponent('RCTVectorDrawableView', iface);
