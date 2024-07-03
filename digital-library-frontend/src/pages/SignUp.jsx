import React, { useState, useContext, useEffect } from 'react';
import {
  FormControl,
  FormLabel,
  Input,
  InputGroup,
  InputLeftAddon,
  Container,
  Box,
  Heading,
  Button,
  Text,
  useToast
} from '@chakra-ui/react';
import { Link, Navigate } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { Context } from "../index"

const SignUp = () => {
  const initialValues = {
    name: "",
    contact: "",
    username: "",
    password: ""
  };
  const [signupInfo, setSignupInfo] = useState(initialValues);
  const [formError, setFormError] = useState({});
  const [submit, setSubmit] = useState(false);

  const { isAuthenticated } = useContext(Context);

  const navigate = useNavigate();
  const toast = useToast();

  useEffect(() => {
    console.log(formError);
    if (Object.keys(formError).length === 0 && submit) {
      handleRegister();
    }
  }, [formError]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setFormError(validateForm(signupInfo));
    setSubmit(true);
  }

  const handleRegister = async () => {
    try {
      const response = await fetch('http://localhost:8080/student/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(signupInfo),
      });

      if (response.ok) {
        await response.json();
        navigate('/login');

        toast({
          title: "Registration successful",
          status: "success",
          duration: 3000,
          isClosable: true,
        });
      } else {
        toast({
          title: "Registration failed",
          description: response.statusText,
          status: "error",
          duration: 3000,
          isClosable: true,
        });
      }
    } catch (error) {
      toast({
        title: "Registration failed",
        description: error.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSignupInfo((prevState) => ({ ...prevState, [name]: value }));
  }

  const validateForm = (values) => {
    const errors = {};
    const phoneRegex = /^[6-9]\d{9}$/;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!values.name) {
      errors.name = "Name is required";
    }
    if (!values.contact) {
      errors.contact = "Contact is required";
    } else if (!phoneRegex.test(values.contact)) {
      errors.contact = "Invalid contact number";
    }
    if (!values.username) {
      errors.username = "Username is required";
    } else if (!emailRegex.test(values.username)) {
      errors.username = "Invalid email format";
    }
    if (!values.password) {
      errors.password = "Password is required";
    }

    return errors;
  }

  if (isAuthenticated) {
    return <Navigate to="/" />;
  }

  return (
    <Container mt={4}>
      <Box textAlign="center">
        <Heading>New User - Sign Up</Heading>
      </Box>
      <form onSubmit={handleSubmit}>
        <FormControl mt={4}>
          <FormLabel htmlFor="name">Name</FormLabel>
          <Input
            id="name"
            name="name"
            type="text"
            placeholder="Your Name"
            value={signupInfo.name}
            onChange={handleChange}
          />
          {formError.name && <Text color="red">{formError.name}</Text>}

          <FormLabel htmlFor="contact" mt={4}>Contact</FormLabel>
          <InputGroup>
            <InputLeftAddon children="+91" />
            <Input
              id="contact"
              name="contact"
              type="tel"
              placeholder="Phone number"
              value={signupInfo.contact}
              onChange={handleChange}
            />
          </InputGroup>
          {formError.contact && <Text color="red">{formError.contact}</Text>}

          <FormLabel htmlFor="username" mt={4}>Username</FormLabel>
          <Input
            id="username"
            name="username"
            type="email"
            placeholder="Your Email"
            value={signupInfo.username}
            onChange={handleChange}
          />
          {formError.username && <Text color="red">{formError.username}</Text>}

          <FormLabel htmlFor="password" mt={4}>Password</FormLabel>
          <Input
            id="password"
            name="password"
            type="password"
            placeholder="Your Password"
            value={signupInfo.password}
            onChange={handleChange}
          />
          {formError.password && <Text color="red">{formError.password}</Text>}
        </FormControl>

        <Box mt={4} display="flex" flexDirection="column" alignItems="center">
          <Button type="submit" colorScheme="blue">
            Register
          </Button>
          <Text mt={2}>
            Already a user?{' '}
            <Link to="/login" style={{ color: 'blue' }}>
              Login here
            </Link>
          </Text>
        </Box>
      </form>
    </Container>
  );
};

export default SignUp;
