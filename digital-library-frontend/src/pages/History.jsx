import React, { useEffect, useState, useContext } from 'react';
import {
  Heading,
  Box,
  useToast,
  Text,
} from '@chakra-ui/react';
import { Context } from '../index';
import { Navigate } from 'react-router-dom';
import Loader from '../components/Loader';
import CustomCard from '../components/CustomCard';

const History = () => {
  const { user, isAuthenticated, refresh, setRefresh, loading, setLoading } = useContext(Context);
  const [student, setStudent] = useState('');

  const toast = useToast();
  const moment = require('moment-timezone');

  useEffect(() => {
    setStudent(user);
  }, [ user,refresh]);

  const returnHandler = async (e, b) => {
    try {
      setLoading(true);
      const token = sessionStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/transaction/return?bookId=${b.id}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        setRefresh(!refresh);
        setLoading(false);
        toast({
          title: "Book returned",
          status: "success",
          duration: 3000,
          isClosable: true,
        });

        console.log('Book returned');
      } else {
        setLoading(false);
        toast({
          title: "Failed to return book",
          description: response.statusText,
          status: "error",
          duration: 3000,
          isClosable: true,
        });
        throw new Error(`Failed to return book: ${response.statusText}`);
      }
    } catch (error) {
      setLoading(false);
      toast({
        title: "Failed to return book",
        description: error,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
      console.error('Failed to return book:', error);
    }
  };

  if (!isAuthenticated) return <Navigate to={'/login'} />;
  if (loading) return <Loader />
  return (
    <Box m={'16'}>

      <Box mt="8">
        <Heading as="h2" size="lg" mb="4">
          Books Issued
        </Heading>

        {
          student.bookList?.length === 0 ? (<Text>No Book Issued</Text>) :
            (
              <Box display='grid' gridTemplateColumns='repeat(auto-fill, minmax(300px, 1fr))' gap={6}>
                {student.bookList?.map((b,index) => {
                  return (
                    <CustomCard key={index} b={b} handler={returnHandler} flag={false} />
                  )
                })}
              </Box>

            )
        }
      </Box>
    </Box>
  );
};

export default History;
