import React, { useContext, useEffect, useState } from 'react';
import { Box, Button, Flex, Heading, Text, useToast } from '@chakra-ui/react';
import PaymentButton from '../components/PaymentButton';
import { Context } from '../index';
import { Navigate } from 'react-router-dom';

const Prime = () => { 

    const {
        user,
        setUser,
        isAuthenticated,
        setIsAuthenticated,
        refresh,
        setRefresh,
      } = useContext(Context);
      const toast = useToast();
      const [isPrime,setIsPrime] = useState(user.prime);

      if (isPrime) {
        toast({
            title: 'Already Prime Member',
            description: "Enjoy the book store.",
            status: 'success',
            duration: 9000,
            isClosable: true,
        });
    }

    if (!isAuthenticated) return <Navigate to={"/login"} />;
    return (
        <Flex direction="column" align="center" p={"16"} minH="100vh">
            <Heading mb={8}>Choose Your Plan</Heading>
            <Flex direction={{ base: 'column', md: 'row' }} gap={8}>
                <Box
                    p={8}
                    shadow="md"
                    borderRadius="lg"
                    textAlign="center"
                    width={{ base: 'full', md: '300px' }}
                >
                    <Heading size="lg">Premium</Heading>
                    <Text mt={4} fontSize="2xl" fontWeight="bold">₹1000</Text>
                    <Text mt={2} >1 year access</Text>
                    <Text mt={2} >3 books limit</Text>

                    <PaymentButton title={"Premium"} amount={1000}/>
                </Box>
            </Flex>
        </Flex>
    );
};

export default Prime;
