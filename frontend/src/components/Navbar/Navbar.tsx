import { Flex, Image } from '@chakra-ui/react';
import React from 'react';
import SearchInput from './SearchInput';
import RightContent from './RightContent/RightContent';

const Navbar: React.FC = () => {
  return (
    <Flex bg='white' height='44px' padding='6px 12px'>
      <Flex align='center'>
        <Image src='/images/redditFace.svg' height='30px' alt=''></Image>
        <Image
          src='/images/redditText.svg'
          height='40px'
          alt=''
          display={{ base: 'none', md: 'unset' }}
        ></Image>
      </Flex>
      <SearchInput />
      {/* <Directory></Directory> */}
      <RightContent></RightContent>
    </Flex>
  );
};
export default Navbar;
