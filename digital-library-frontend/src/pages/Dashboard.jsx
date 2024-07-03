import React, { useEffect, useState, useContext, useMemo } from 'react';
import {
  Box,
  Heading,
  Input,
  Radio,
  RadioGroup,
  Stack,
  useToast,
} from '@chakra-ui/react';
import axios from 'axios';
import { Navigate } from 'react-router-dom';
import { Context } from '../index';
import Loader from '../components/Loader';
import CustomCard from '../components/CustomCard';
import Pagination from '../components/Pagination'; // Import the Pagination component

const Dashboard = () => {
  const { user, setUser, isAuthenticated, refresh, setRefresh, loading, setLoading } = useContext(Context);

  const [student, setStudent] = useState({});
  const [book, setBook] = useState([]);
  const [query, setQuery] = useState('');
  const [value, setValue] = useState('name');
  const [currPage, setCurrPage] = useState(1);

  const toast = useToast();


  useEffect(() => {
    if (isAuthenticated) {
      fetchData();
      console.log(book);
    }
  }, [refresh, isAuthenticated]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const token = sessionStorage.getItem('token');
      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.get('http://localhost:8080/book/frontend', {
        headers: headers,
      });

      setBook(response.data);
      setLoading(false);
    } catch (error) {
      setLoading(false);
      toast({
        title: 'Failed to fetch book',
        description: 'Unable to fetch books at this moment',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const issueHandler = async (e, book) => {
    e.preventDefault();
    try {
      setLoading(true);
      const token = sessionStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/transaction/issue?name=${book.name}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        setUser(student);
        setLoading(false);
        setRefresh(!refresh);
        console.log('Book issued');

        toast({
          title: 'Book issued',
          status: 'success',
          duration: 3000,
          isClosable: true,
        });
      } else {
        setLoading(false);
        console.log(response);
        throw new Error(`Failed to issue book: ${response.statusText}`);
      }
    } catch (error) {
      console.log(error);
      toast({
        title: 'Failed to issue book',
        description: 'Book limit reached / Not a prime member',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const filteredData = useMemo(
    () => book?.filter((item) => item[value].toLowerCase().includes(query.toLowerCase())),
    [book, query, value]
  );

  const totalPages = Math.ceil(filteredData.length / 8);

  const handlePageChange = (page) => {
    setCurrPage(page);
  };

  const paginatedData = useMemo(
    () => filteredData.slice((currPage - 1) * 8, currPage * 8),
    [filteredData, currPage]
  );

  if (!isAuthenticated) return <Navigate to={'/login'} />;
  if (loading) return <Loader />;

  return (
    <Box m={'16'}>
      <Heading as='h2' size='lg' mb='4'>
        Available Books
      </Heading>

      <Box display='flex' mb='4' alignItems='center'>
        <RadioGroup onChange={(value) => setValue(value)} value={value}>
          <Stack direction='row'>
            <Radio value='name'>Book name</Radio>
            <Radio value='authorName'>Author name</Radio>
            <Radio value='genre'>Book Genre</Radio>
          </Stack>
        </RadioGroup>

        <Input
          placeholder='Enter parameter to search'
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          ml='4'
        />
      </Box>

      <Box display='grid' gridTemplateColumns='repeat(auto-fill, minmax(300px, 1fr))' gap={6}>
        {paginatedData?.map((b, index) => (
          <CustomCard key={index} b={b} handler={issueHandler} flag={true}/>
        ))}
      </Box>

      <Pagination
        currentPage={currPage}
        totalPages={totalPages}
        onPageChange={handlePageChange}
      />
    </Box>
  );
};

export default Dashboard;
