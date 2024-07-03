import { Avatar, Button, Stack } from '@chakra-ui/react'
import React, { useContext } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Context } from "../index"
import PaymentButton from './PaymentButton'

const Header = () => {
    const { user, setUser } = useContext(Context);

    const navigate = useNavigate();

    const navigateToProfile = () => {
        navigate('/profile')
    };

    return (
        <Stack direction={['column', 'row']} p={"4"} shadow={"base"} bgColor={"blackAlpha.700"} alignItems="center" justifyContent={'space-between'}>

            <Stack direction={['column', 'row']} spacing="2">
                <Button variant={"unstyled"} color={"white"} p={"2"}>
                    <Link to={"/"}>Home</Link>
                </Button>

                <Button variant={"unstyled"} color={"white"} p={'2'}>
                    <Link to={"/dashboard"}>Library</Link>
                </Button>

                <Button variant={"unstyled"} color={"white"} p={"2"}>
                    <Link to={"/history"}>Issued-Books</Link>
                </Button>

                <Button variant={"unstyled"} color={"white"} p={"2"}>
                    <Link to={"/txn-history"}>Transaction-History</Link>
                </Button>

                <Button variant={"unstyled"} color={"white"} p={"2"}>
                    <Link to={"/prime"}>Become Prime</Link>
                </Button>

                {/* <PaymentButton/> */}
            </Stack>


            <Avatar name={user.name}
                size={'md'}
                onClick={navigateToProfile}
                style={{ cursor: 'pointer' }}>
            </Avatar>

        </Stack>
    )
}

export default Header