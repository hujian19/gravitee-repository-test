/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.repository.config.mock;

import io.gravitee.repository.management.api.UserRepository;
import io.gravitee.repository.management.model.User;
import org.mockito.ArgumentMatcher;

import java.util.Date;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class UserRepositoryMock extends AbstractRepositoryMock<UserRepository> {

    public UserRepositoryMock() {
        super(UserRepository.class);
    }

    @Override
    void prepare(UserRepository userRepository) throws Exception {
        final User user = mock(User.class);
        when(user.getPassword()).thenReturn("New pwd");
        final User user4 = mock(User.class);

        final User userUpdated = mock(User.class);
        when(userUpdated.getId()).thenReturn("id2update");
//        when(userUpdated.getUsername()).thenReturn("usernameUpdated");
        when(userUpdated.getSource()).thenReturn("sourceUpdated");
        when(userUpdated.getSourceId()).thenReturn("sourceIdUpdated");
        when(userUpdated.getPassword()).thenReturn("passwordUpdated");
        when(userUpdated.getEmail()).thenReturn("emailUpdated");
        when(userUpdated.getFirstname()).thenReturn("firstnameUpdated");
        when(userUpdated.getLastname()).thenReturn("lastnameUpdated");
        when(userUpdated.getPicture()).thenReturn("pictureUpdated");
        when(userUpdated.getCreatedAt()).thenReturn(new Date(1439032010883L));
        when(userUpdated.getUpdatedAt()).thenReturn(new Date(1439042010883L));
        when(userUpdated.getLastConnectionAt()).thenReturn(new Date(1439052010883L));

        io.gravitee.common.data.domain.Page<User> searchResult = new io.gravitee.common.data.domain.Page<>(
                asList(user, mock(User.class), mock(User.class), mock(User.class), mock(User.class), mock(User.class), mock(User.class), mock(User.class)),0, 0, 8);

        when(userRepository.search(any())).thenReturn(searchResult);
        when(userRepository.create(any(User.class))).thenReturn(user);
        when(userRepository.findById("user0")).thenReturn(of(user));
        when(userRepository.findById("id2update")).thenReturn(of(userUpdated));

//        when(userRepository.findByUsername("createuser1")).thenReturn(of(user));
//        when(userRepository.findByUsername("user0 name")).thenReturn(of(user));
//        when(user.getUsername()).thenReturn("createuser1");
        when(user.getId()).thenReturn("createuser1");
        when(user.getEmail()).thenReturn("createuser1@gravitee.io");

        when(userRepository.findBySource("gravitee", "createuser1")).thenReturn(of(user));

        when(userRepository.update(argThat(new ArgumentMatcher<User>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof User && "id2update".equals(((User) o).getId());
            }
        }))).thenReturn(userUpdated);
        when(userRepository.update(argThat(new ArgumentMatcher<User>() {
            @Override
            public boolean matches(Object o) {
                return o == null || (o instanceof User && "unknown".equals(((User) o).getId()));
            }
        }))).thenThrow(new IllegalStateException());

        final User user1 = mock(User.class);
        when(user1.getId()).thenReturn("user1");

        final User user5 = mock(User.class);
        when(user5.getId()).thenReturn("user5");

        when(userRepository.findBySource("gravitee", "user1")).thenReturn(of(user1));
        when(userRepository.findById("user1")).thenReturn(of(user1));
        when(userRepository.findByIds(asList("user1", "user5"))).thenReturn(new HashSet<>(asList(user1, user5)));

        when(userRepository.findById("user2delete")).thenReturn(of(new User()), empty());
    }
}
