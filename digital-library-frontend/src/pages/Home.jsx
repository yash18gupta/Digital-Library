import { Box, Heading, Image, Stack, Text } from '@chakra-ui/react';
import React,{useState,useEffect,useContext} from 'react';
import { Carousel } from 'react-responsive-carousel';
import { useTypewriter } from 'react-simple-typewriter';
import 'react-responsive-carousel/lib/styles/carousel.min.css'; // Import the carousel styles
import libimg1 from '../images/library1.png';
import libimg2 from '../images/library2.png'
import libimg3 from '../images/library3.png'
import {Context} from "../index"
import { Navigate } from 'react-router-dom';


const Home = () => {


  const {user,isAuthenticated}  = useContext(Context);

  console.log(user);

  if (!isAuthenticated) return <Navigate to={"/login"} />;

  return (
    <Box
      width={'100vw'}
      height={'95vh'}
      display={'flex'}
      flexDirection={'column'}
      alignItems={'center'}
      justifyContent={'center'}
    >
      <Heading as="h1" size="xl" mb="4">
        Hi - {user.name}
      </Heading>
      <Stack
        width={'70vw'}
        height={'60vh'}
        direction={['column', 'row']}
        bgColor={'blackAlpha.100'}
        justifyContent={'space-between'}
        alignItems={'center'}
        p={'4'}
        borderRadius={'40px'}
      >
        <Box minw={'30%'} h={['40', '300']}>
          <MyCarousel />
        </Box>
        <Box minW={'60%'} minH={'30vh'} bgColor={'blackAlpha.200'} display={'flex'} flexDirection={'column'} alignItems={'center'} justifyContent={'center'} borderRadius={'40px'}>
          <Text fontSize={'3rem'} fontWeight={'bold'} color={'orange'}>
            <TypewriterText/>
          </Text>
        </Box>

      </Stack>
    </Box>
  );
};

// Typewriter effect component
const TypewriterText = () => {
  const [text] = useTypewriter({
    words: ['Welcome to Digital Library!', "India's Largest Digital Library!"],
    loop: {},
    typeSpeed: 120,
    deleteSpeed: 80,
  });
  return text;
};

// Carousel component
const MyCarousel = () => {
  return (
    <Carousel
      showThumbs={false}
      autoPlay={true}
      infiniteLoop={true}
      interval={3000}
      showStatus={false}
      showArrows={false}
    >
      <div>
        <Image src={libimg1} borderRadius={'40px'}/>
      </div>
      <div>
        <Image src={libimg2} borderRadius={'40px'}/>
      </div>
      <div>
        <Image src={libimg3} borderRadius={'40px'}/>
      </div>
    </Carousel>
  );
};

export default Home;
