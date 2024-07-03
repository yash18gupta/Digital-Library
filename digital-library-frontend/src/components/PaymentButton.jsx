import React, { useContext, useState } from 'react';
import { Context } from '../index';
import { Button, useToast } from '@chakra-ui/react';

const PaymentButton = ({ amount, title }) => {
    const toast = useToast();
    const { user, setUser,refresh,setRefresh } = useContext(Context);
    const [txnAmount, setTxnAmount] = useState(user.primeTransactionDetails ? user.primeTransactionDetails.amount / 100 : 0);

    const loadRazorpay = async () => {
        const token = sessionStorage.getItem('token');
        const headers = {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
        };
        const res = await loadScript('https://checkout.razorpay.com/v1/checkout.js');

        if (!res) {
            toast({
                title: 'Error',
                description: 'Razorpay SDK failed to load. Are you online?',
                status: 'error',
                duration: 9000,
                isClosable: true,
            });
            return;
        }

        const data = await fetch(`http://localhost:8080/prime/${amount}`, { method: 'GET', headers: headers }).then((t) =>
            t.json()
        );

        const options = {
            key: 'rzp_test_Np1bKPqhESiAqc',
            amount: data.amount.toString(),
            currency: data.currency,
            name: "Digital Library",
            description: "Test Transaction",
            order_id: data.orderId,
            handler: function (response) {
                // Handle success response
                toast({
                    title: 'Payment Successful',
                    description: `Payment ID: ${response.razorpay_payment_id}`,
                    status: 'success',
                    duration: 9000,
                    isClosable: true,
                });
                setRefresh(!refresh);
            },
            prefill: {
                name: user.name,
                email: user.email,
                contact: user.contact
            },
            notes: {
                address: "Razorpay Corporate Office"
            },
            theme: {
                color: "#3399cc"
            }
        };

        const paymentObject = new window.Razorpay(options);
        paymentObject.on('payment.failed', function (response) {
            // Handle failed response
            toast({
                title: 'Payment Failed',
                description: response.error.description,
                status: 'error',
                duration: 9000,
                isClosable: true,
            });
        });
        paymentObject.open();
    };

    const loadScript = (src) => {
        return new Promise((resolve) => {
            const script = document.createElement('script');
            script.src = src;
            script.onload = () => {
                resolve(true);
            };
            script.onerror = () => {
                resolve(false);
            };
            document.body.appendChild(script);
        });
    };

    console.log(user);

    return (
        <Button variant={"solid"} colorScheme='teal' p={"2"} onClick={loadRazorpay} isDisabled={user.prime===true}>
            {title}
        </Button>
    );
};

export default PaymentButton;
