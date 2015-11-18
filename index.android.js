var { requireNativeComponent, PropTypes } = require('react-native');

var iface = {
    name: 'VectorDrawableView',
    propTypes: {
        resourceName: PropTypes.string
    }
};

module.exports = requireNativeComponent('RCTVectorDrawableView', iface);
