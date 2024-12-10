/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import CFS from './src/CFS';
import {name as appName} from './app.json';

// Register the main app component
AppRegistry.registerComponent(appName, () => App);

// Register the CFS component for the external display
AppRegistry.registerComponent('CFS', () => CFS);
