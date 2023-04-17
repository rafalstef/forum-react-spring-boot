import { Flex } from '@chakra-ui/react';
import React from 'react';
import AuthButtons from './AuthButtons';
import AuthModal from '@/components/Modal/Auth/AuthModal';

type RightContentProps = {};

const RightContent: React.FC<RightContentProps> = () => {
  return (
    <>
      <Flex justify='center' align='center'>
        <AuthModal />
        <AuthButtons />
      </Flex>
    </>
  );
};

export default RightContent;
