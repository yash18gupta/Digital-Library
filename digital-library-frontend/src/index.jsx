import { ChakraProvider, ColorModeScript } from '@chakra-ui/react';
import React, { useState,StrictMode ,createContext, useEffect} from 'react';
import * as ReactDOM from 'react-dom/client';
import App from './App';
import ColorModeSwitcher from "./ColorModeSwitcher"
import "./styles/app.scss"
import axios from 'axios';

const container = document.getElementById('root');
const root = ReactDOM.createRoot(container);

export const Context = createContext({isAuthenticated:false,user:{}});

const AppWrapper = ()=>{
  const [isAuthenticated,setIsAuthenticated] = useState(false);
  const [user,setUser] = useState({});
  const [refresh,setRefresh] = useState(false);
  const [loading,setLoading] = useState(false);

  return(
    <Context.Provider value={{isAuthenticated,setIsAuthenticated,user,setUser,refresh,setRefresh,loading,setLoading}}>
      <App />
    </Context.Provider> 
  )
}

root.render(
  <StrictMode>
    <ColorModeScript />
    <ChakraProvider>
      <ColorModeSwitcher/>
      <AppWrapper/>
    </ChakraProvider>
  </StrictMode>
);

