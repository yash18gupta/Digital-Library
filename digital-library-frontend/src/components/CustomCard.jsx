import React from 'react'
import {
    Button,
    ButtonGroup,
    Card,
    CardBody,
    CardFooter,
    Divider,
    Heading,
    Image,
    Stack,
    Text,
    useToast
} from '@chakra-ui/react';
import axios from 'axios';
import { DownloadIcon } from '@chakra-ui/icons';
const CustomCard = ({ b, handler, flag }) => {
    const toast = useToast();

    const downloadHandler = async (e, b) => {
        const token = sessionStorage.getItem('token');
        const headers = {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
        };
    
        try {
            const response = await axios.get(`http://localhost:8080/book/download/${b.fileData.id}`, {
                headers: headers,
                responseType: 'blob', // Important to handle binary data
            });
    
            if (response.status === 200) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', `${b.name}.pdf`); // Use the desired file name and extension
                document.body.appendChild(link);
                link.click();
                link.remove();
    
                toast({
                    title: 'Book downloaded',
                    status: 'success',
                    duration: 3000,
                    isClosable: true,
                });
            }
        } catch (error) {
            console.error('Error downloading the book:', error);
            toast({
                title: 'Error downloading the book',
                status: 'error',
                duration: 3000,
                isClosable: true,
            });
        }
    }
    

    return (
        <Card width='300px' display='flex' flexDirection='column'>
            <CardBody flex='1'>
                <Image src={b.imageUrl} alt='No preview' borderRadius='lg' />
                <Stack mt='6' spacing='3'>
                    <Heading size='md'>{b.name}</Heading>

                    {
                        flag ? <Text>Author: {b.authorName}</Text> : <Text>Author: {b.author.name}</Text>
                    }
                    <Text>Genre: {b.genre}</Text>
                    <Text>Pages: {b.pages}</Text>
                    {
                        flag ? <Text>Count: {b.count}</Text> : <></>
                    }
                </Stack>
            </CardBody>
            <Divider />
            <CardFooter>
                <ButtonGroup spacing='2'>
                    <Button variant='solid' colorScheme='blue' onClick={(e) => handler(e, b)}>
                        {flag ? 'Issue' : 'Return'}
                    </Button>
                    {
                        !flag ? <Button onClick={(e) => downloadHandler(e,b)}><DownloadIcon /></Button> : <></>
                    }
                </ButtonGroup>
            </CardFooter>
        </Card>
    );
};

export default CustomCard