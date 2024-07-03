import React, { useEffect, useState, useContext } from 'react';
import {
    Table,
    Thead,
    Tbody,
    Tr,
    Th,
    Td,
    TableContainer,
    Heading,
    Box,
    useToast,
    Text,
    Badge
} from '@chakra-ui/react';
import { PaginationTable } from "table-pagination-chakra-ui"
import { Context } from '../index';
import { Navigate } from 'react-router-dom';
import axios from 'axios';
import Loader from '../components/Loader';

const TransactionHistory = () => {
    const { user, isAuthenticated, refresh, setRefresh, loading, setLoading } = useContext(Context);
    const [transaction, setTransaction] = useState([]);
    const [pageIndex, setPageIndex] = useState(0);
    const [pageSize, setPageSize] = useState(10);

    const toast = useToast();
    const moment = require('moment-timezone');

    useEffect(() => {
        if (isAuthenticated) {
          fetchData();
        }
      }, [refresh, isAuthenticated]);

    const fetchData = async () => {
        try {
            setLoading(true);
            const token = sessionStorage.getItem('token');
            const headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            };

            const response = await axios.get('http://localhost:8080/transaction/getAll', {
                headers: headers
            });

            console.log(response.data);
            setTransaction(response.data);
            setLoading(false);

        } catch (error) {
            setLoading(false);
            console.log(error);
        }
    };

    if (!isAuthenticated) return <Navigate to={'/login'} />;
    if (loading) return <Loader />
    return (
        <Box m={'16'}>
            <Box mt="8">
                <Heading as="h2" size="lg" mb="4">
                    Transaction History
                </Heading>
                {
                    transaction?.length === 0 ? (<Text>No Txn</Text>) :
                        <TableContainer>
                            <Table variant="striped">
                                <Thead>
                                    <Tr>
                                        <Th>External Txn Id</Th>
                                        <Th>Book Name</Th>
                                        <Th>Fine</Th>
                                        <Th>Txn Status</Th>
                                        <Th>Txn Type</Th>
                                        <Th>Txn Time</Th>
                                    </Tr>
                                </Thead>
                                <Tbody>
                                    {transaction
                                        ?.sort((a, b) => new Date(b.updatedOn) - new Date(a.updatedOn))
                                        .map((transaction) => {
                                            const date = moment.tz(transaction.updatedOn, 'Asia/Kolkata');

                                            const formattedDate = date.format('MMMM Do YYYY, h:mm:ss a');
                                            return (
                                                <Tr key={transaction.id}>
                                                    <Td>{transaction.externalTxnId}</Td>
                                                    <Td>{transaction.book.name}</Td>
                                                    <Td>{transaction.fine}</Td>
                                                    <Td>{transaction.transactionStatus === "SUCCESS" ? (
                                                        <Badge colorScheme="green">SUCCESS</Badge>
                                                    ) : transaction.transactionStatus === "PENDING" ? (
                                                        <Badge colorScheme="yellow">PENDING</Badge>
                                                    ) : (
                                                        <Badge colorScheme="red">FAILED</Badge>
                                                    )}</Td>
                                                    <Td>{transaction.transactionType === "ISSUE" ? <Badge colorScheme='green'>ISSUE</Badge> : <Badge colorScheme='red'>RETURN</Badge>}</Td>
                                                    <Td>{formattedDate}</Td>
                                                </Tr>
                                            )
                                        }).slice(pageSize * pageIndex, pageSize * (pageIndex + 1))
                                        }
                                </Tbody>
                            </Table>
                            <PaginationTable
                                pageSize={pageSize}
                                setPageSize={setPageSize}
                                pageIndex={pageIndex}
                                setPageIndex={setPageIndex}
                                totalItemsCount={transaction.length}
                                pageSizeOptions={[10, 20, 30]}
                            />
                        </TableContainer>

                }

            </Box>

        </Box>
    )
}

export default TransactionHistory