import React from 'react'
import { Box, Spinner } from '@chakra-ui/react'

const Loader = () => {
    return (
        <Box
            width={'100vw'}
            height={'95vh'}
            display={'flex'}
            alignItems={'center'}
            justifyContent={'center'}
        >

            <Spinner
                thickness='4px'
                speed='0.65s'
                emptyColor='gray.200'
                color='blue.500'
                size='xl'
            />
        </Box>

    )
}

export default Loader