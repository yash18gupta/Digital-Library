import React, { useState ,useContext} from 'react'
import { useToast } from "@chakra-ui/react";
import {
    FormControl,
    FormLabel,
    Input,
    Container,
    Box,
    Heading,
    Button,
    Text,
} from '@chakra-ui/react';
import { Link ,useNavigate ,Navigate} from 'react-router-dom';
import {Context} from "../index"

const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const {isAuthenticated,setIsAuthenticated}  = useContext(Context);

    const navigate = useNavigate();
    const toast = useToast();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/auth/login',{
                method : 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if(response.ok){
                const data = await response.json();

                sessionStorage.setItem('token', data.jwtToken);
                setIsAuthenticated(true);

                toast({
                    title: "Login successful",
                    status: "success",
                    duration: 3000,
                    isClosable: true,
                });
                navigate('/')
            }
            else {
                setIsAuthenticated(false);
                toast({
                    title: "Login failed",
                    description: response.statusText,
                    status: "error",
                    duration: 3000,
                    isClosable: true,
                  });
              }
        } catch (error) {
            setIsAuthenticated(false);
            toast({
                title: "Login failed",
                description: "An error occurred while logging in",
                status: "error",
                duration: 3000,
                isClosable: true,
            });
        }
    }

    if(isAuthenticated) {
        return (
          <Navigate to={"/"}/>
        )
      }
    return (
        <Container mt={4}>
            <Box textAlign="center">
                <Heading>Login</Heading>
            </Box>
            <FormControl mt={4}>
            
                <FormLabel htmlFor="username" mt={4}>Username</FormLabel>
                <Input
                    id="username"
                    type="email"
                    placeholder="Your Email"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <FormLabel htmlFor="password" mt={4}>Password</FormLabel>
                <Input
                    id="password"
                    type="password"
                    placeholder="Your Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </FormControl>

            <Box mt={4} display={'flex'} flexDirection={'column'} alignItems={'center'}>
                <Button onClick={handleLogin} colorScheme="blue">
                    Login
                </Button>
                <Text mt={2}>
                    New user?{' '}
                    <Link to="/signup" style={{color:'blue'}}>
                        Register here
                    </Link>
                </Text>
            </Box>
        </Container>
    )
}

export default Login