var { requireNativeComponent, PropTypes } = require('react-native');
var View = require('View');

var iface = {
    name: 'VectorDrawableView',
    propTypes: {
        ...View.propTypes,
        resourceName: PropTypes.string,
        animations: PropTypes.array
    }
};

module.exports = requireNativeComponent('RCTVectorDrawableView', iface);
