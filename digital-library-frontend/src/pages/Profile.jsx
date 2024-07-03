import React, { useEffect, useState, useContext } from 'react';
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
  Stack,
  VStack,
  useToast,
} from '@chakra-ui/react';
import { Context } from '../index';
import { Navigate } from 'react-router-dom';
import CustomModal from '../components/CustomModal';

const Profile = () => {
  const [student, setStudent] = useState({});
  const [readOnly, setReadOnly] = useState(true);
  const toast = useToast();
  const {
    user,
    setUser,
    isAuthenticated,
    setIsAuthenticated,
    refresh,
    setRefresh,
  } = useContext(Context);

  const [openLogout, setOpenLogout] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const moment = require('moment-timezone');

  useEffect(() => {
    setStudent(user);
  }, [user]);

  if (!isAuthenticated) return <Navigate to={'/login'} />;

  const logoutHandler = async () => {
    try {
      sessionStorage.removeItem('token');
      setIsAuthenticated(false);
      setUser({});

      toast({
        title: 'Logout success',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Logout failed:', error.message);

      toast({
        title: 'Logout failed',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const deleteHandler =async()=>{
    try {
      const token = sessionStorage.getItem('token');
      const response = await fetch('http://localhost:8080/student/delete', {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        setIsAuthenticated(false);
        setUser({});
        setRefresh(!refresh);

        toast({
          title: 'Profile deletion success',
          status: 'success',
          duration: 3000,
          isClosable: true,
        });


      } else {
        // If the request was not successful, throw an error or handle it accordingly
        toast({
          title: 'Profile deletion failed',
          description: response.statusText,
          status: 'error',
          duration: 3000,
          isClosable: true,
        });

        throw new Error(
          `Failed to update student information: ${response.statusText}`
        );
      }
    } catch (error) {
      toast({
        title: 'Profile deletion failed',
        description: error,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
      console.error('Error deleting student information:', error);
    }
  }

  const handleChange = (event, field, flag) => {
    if (!flag) {
      setStudent({
        ...student,
        [field]: event.target.value,
      });
    } else {
      setStudent({
        ...student,
        securedUser: {
          ...student.securedUser,
          [field]: event.target.value,
        },
      });
    }
  };

  const editHandler = () => {
    setReadOnly(false);
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const token = sessionStorage.getItem('token');
      const response = await fetch('http://localhost:8080/student/update', {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: student.name,
          contact: student.contact,
        }),
      });

      // Check if the request was successful (status code 2xx)
      if (response.ok) {
        setUser(student);
        setRefresh(!refresh);
        setReadOnly(true);

        toast({
          title: 'Profile updation success',
          status: 'success',
          duration: 3000,
          isClosable: true,
        });

        console.log('Student information updated successfully');
        // Optionally, you can handle any additional logic here
      } else {
        // If the request was not successful, throw an error or handle it accordingly
        toast({
          title: 'Profile updation failed',
          description: response.statusText,
          status: 'error',
          duration: 3000,
          isClosable: true,
        });

        throw new Error(
          `Failed to update student information: ${response.statusText}`
        );
      }
    } catch (error) {
      toast({
        title: 'Profile updation failed',
        description: error,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
      console.error('Error updating student information:', error);
    }
  };


  const primeDate = moment.tz(student?.primeValidity, 'Asia/Kolkata');

  const primeValidity = primeDate.format('MMMM Do YYYY, h:mm:ss a')

  return (
    <Container mt={4}>
      <Box textAlign="center">
        <Heading>Profile</Heading>
      </Box>
      <FormControl mt={4}>
        <FormLabel htmlFor="name">Name</FormLabel>
        <Input
          id="name"
          type="text"
          placeholder="Your Name"
          value={student.name}
          isReadOnly={readOnly}
          onChange={e => handleChange(e, 'name', false)}
        />

        <FormLabel htmlFor="contact" mt={4}>
          Contact
        </FormLabel>
        <InputGroup>
          <InputLeftAddon children="+91" />
          <Input
            id="contact"
            type="tel"
            placeholder="Phone number"
            value={student.contact}
            isReadOnly={readOnly}
            onChange={e => handleChange(e, 'contact', false)}
          />
        </InputGroup>

        <FormLabel htmlFor="username" mt={4}>
          Username
        </FormLabel>
        <Input
          id="username"
          type="email"
          placeholder="Your Email"
          value={student.securedUser?.username}
          isReadOnly={true}
          onChange={e => handleChange(e, 'username', true)}
        />

        <FormLabel htmlFor="primeStatus" mt={4}>
          PrimeStatus
        </FormLabel>
        <Input
          id="primeStatus"
          type="text"
          placeholder="Prime Status"
          value={student?.prime ? `Yes - ${primeValidity}` : "No"}
          isReadOnly={readOnly}
        />
      </FormControl>

      <VStack mt={'4'}>
        <Stack direction={['column', 'row']}>
          {readOnly ? <Button onClick={editHandler}>Edit</Button> : <></>}
          {!readOnly ? (
            <Button onClick={() => setReadOnly(true)}>Cancel</Button>
          ) : (
            <></>
          )}
          {!readOnly ? <Button onClick={handleSubmit}>Save</Button> : <></>}
        </Stack>

        <Button onClick={() => setOpenLogout(true)}>Logout</Button>
        <CustomModal
          isOpen={openLogout}
          onClose={() => setOpenLogout(false)}
          title={'Logout'}
          children={'Do you want to logout?'}
          handler={logoutHandler}
        />
        <Button onClick={() => setOpenDelete(true)} colorScheme="red">
          Delete Account
        </Button>
        <CustomModal
          isOpen={openDelete}
          onClose={() => setOpenDelete(false)}
          title={'Delete Account'}
          children={'Do you want delete account?'}
          handler={deleteHandler}
        />
      </VStack>
    </Container>
  );
};

export default Profile;
