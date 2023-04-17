import React from 'react';
import {
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
  Flex,
} from '@chakra-ui/react';
import { authModalState } from '@/atoms/authModelAtom';
import { useRecoilState } from 'recoil';
import AuthInputs from './AuthInputs';
import ResetPassword from './ResetPassword';
// import { Button } from '@/chakra/button';

const AuthModal: React.FC = () => {
  const [modalState, setModalState] = useRecoilState(authModalState);

  const handleClose = () => {
    setModalState((prev) => ({
      ...prev,
      open: false,
    }));
  };

  return (
    <>
      <Modal isOpen={modalState.open} onClose={handleClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader textAlign='center'>
            {modalState.view === 'login' && 'Login'}
            {modalState.view === 'signup' && 'Sign Up'}
            {modalState.view === 'resetPassword' && 'Reset Password'}
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody
            display='flex'
            flexDirection='column'
            alignItems='center'
            justifyContent='center'
            pb={6}
          >
            <Flex
              direction='column'
              align='center'
              justify='center'
              width='70%'
            >
              {modalState.view === 'login' || modalState.view === 'signup' ? (
                <>{<AuthInputs />}</>
              ) : (
                <>{<ResetPassword />}</>
              )}
            </Flex>
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};

export default AuthModal;
