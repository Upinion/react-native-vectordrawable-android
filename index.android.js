var { requireNativeComponent, PropTypes } = require('react-native');

var iface = {
    name: 'VectorDrawableView',
    propTypes: {
        resourceName: PropTypes.string,
        vectorAnimation: PropTypes.object
    }
};

module.exports = requireNativeComponent('RCTVectorDrawableView', iface,
    {nativeOnly: {
        'testID': true,
        'accessibilityComponentType': true,
        'renderToHardwareTextureAndroid': true,
        'translateY': true,
        'translateX': true,
        'accessibilityLabel': true,
        'accessibilityLiveRegion': true,
        'importantForAccessibility': true,
        'rotation': true,
        'opacity': true,
        'onLayout': true
        }
    });
