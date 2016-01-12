var { requireNativeComponent, PropTypes } = require('react-native');

var iface = {
    name: 'VectorDrawableView',
    propTypes: {
        resourceName: PropTypes.string,
        animations: PropTypes.array
    }
};

module.exports = requireNativeComponent('RCTVectorDrawableView', iface);
